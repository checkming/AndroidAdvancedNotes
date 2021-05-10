/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okhttp3.internal.cache;

import java.util.Date;

import javax.annotation.Nullable;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.http.HttpDate;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.StatusLine;

import static java.net.HttpURLConnection.HTTP_BAD_METHOD;
import static java.net.HttpURLConnection.HTTP_GONE;
import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_NOT_AUTHORITATIVE;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NOT_IMPLEMENTED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_REQ_TOO_LONG;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Given a request and cached response, this figures out whether to use the network, the cache, or
 * both.
 *
 * <p>Selecting a cache strategy may add conditions to the request (like the "If-Modified-Since"
 * header for conditional GETs) or warnings to the cached response (if the cached data is
 * potentially stale).
 */
public final class CacheStrategy {
    /**
     * The request to send on the network, or null if this call doesn't use the network.
     */
    public final @Nullable
    Request networkRequest;

    /**
     * The cached response to return or validate; or null if this call doesn't use a cache.
     */
    public final @Nullable
    Response cacheResponse;

    CacheStrategy(Request networkRequest, Response cacheResponse) {
        this.networkRequest = networkRequest;
        this.cacheResponse = cacheResponse;
    }

    /**
     * Returns true if {@code response} can be stored to later serve another request.
     */
    public static boolean isCacheable(Response response, Request request) {
        // Always go to network for uncacheable response codes (RFC 7231 section 6.1),
        // This implementation doesn't support caching partial content.
        switch (response.code()) {
            //todo 对于 200, 203, 204, 300, 301, 404, 405, 410, 414, 501, 308 只判断是不是存在
            // cache-control:nostore 不缓存
            // 302, 307 需要存在: Expires:时间、CacheControl:max-age/public/private 才判断是不是存在
            // cache-control:nostore
            // 才能够缓存
            // 其他响应码不缓存
            case HTTP_OK:
            case HTTP_NOT_AUTHORITATIVE:
            case HTTP_NO_CONTENT:
            case HTTP_MULT_CHOICE:
            case HTTP_MOVED_PERM:
            case HTTP_NOT_FOUND:
            case HTTP_BAD_METHOD:
            case HTTP_GONE:
            case HTTP_REQ_TOO_LONG:
            case HTTP_NOT_IMPLEMENTED:
            case StatusLine.HTTP_PERM_REDIRECT:
                // These codes can be cached unless headers forbid it.
                break;

            case HTTP_MOVED_TEMP:
            case StatusLine.HTTP_TEMP_REDIRECT:
                // These codes can only be cached with the right response headers.
                // http://tools.ietf.org/html/rfc7234#section-3
                // s-maxage is not checked because OkHttp is a private cache that should ignore
                // s-maxage.
                if (response.header("Expires") != null
                        || response.cacheControl().maxAgeSeconds() != -1
                        || response.cacheControl().isPublic()
                        || response.cacheControl().isPrivate()) {
                    break;
                }
                // Fall-through.

            default:
                // All other codes cannot be cached.
                return false;
        }

        // A 'no-store' directive on request or response prevents the response from being cached.
        return !response.cacheControl().noStore() && !request.cacheControl().noStore();
    }

    public static class Factory {
        final long nowMillis;
        final Request request;
        final Response cacheResponse;

        /**
         * The server's time when the cached response was served, if known.
         */
        private Date servedDate;
        private String servedDateString;

        /**
         * The last modified date of the cached response, if known.
         */
        private Date lastModified;
        private String lastModifiedString;

        /**
         * The expiration date of the cached response, if known. If both this field and the max
         * age are
         * set, the max age is preferred.
         */
        private Date expires;

        /**
         * Extension header set by OkHttp specifying the timestamp when the cached HTTP request was
         * first initiated.
         */
        private long sentRequestMillis;

        /**
         * Extension header set by OkHttp specifying the timestamp when the cached HTTP response was
         * first received.
         */
        private long receivedResponseMillis;

        /**
         * Etag of the cached response.
         */
        private String etag;

        /**
         * Age of the cached response.
         */
        private int ageSeconds = -1;

        public Factory(long nowMillis, Request request, Response cacheResponse) {
            this.nowMillis = nowMillis;
            this.request = request;
            this.cacheResponse = cacheResponse;

            if (cacheResponse != null) {
                this.sentRequestMillis = cacheResponse.sentRequestAtMillis();
                this.receivedResponseMillis = cacheResponse.receivedResponseAtMillis();
                Headers headers = cacheResponse.headers();
                for (int i = 0, size = headers.size(); i < size; i++) {
                    String fieldName = headers.name(i);
                    String value = headers.value(i);
                    if ("Date".equalsIgnoreCase(fieldName)) {
                        servedDate = HttpDate.parse(value);
                        servedDateString = value;
                    } else if ("Expires".equalsIgnoreCase(fieldName)) {
                        expires = HttpDate.parse(value);
                    } else if ("Last-Modified".equalsIgnoreCase(fieldName)) {
                        lastModified = HttpDate.parse(value);
                        lastModifiedString = value;
                    } else if ("ETag".equalsIgnoreCase(fieldName)) {
                        etag = value;
                    } else if ("Age".equalsIgnoreCase(fieldName)) {
                        ageSeconds = HttpHeaders.parseSeconds(value, -1);
                    }
                }
            }
        }

        /**
         * Returns a strategy to satisfy {@code request} using the a cached response {@code
         * response}.
         */
        public CacheStrategy get() {
            CacheStrategy candidate = getCandidate();
            //todo 如果可以使用缓存，那networkRequest必定为null；指定了只使用缓存但是networkRequest又不为null，冲突。那就gg(拦截器返回504)
            if (candidate.networkRequest != null && request.cacheControl().onlyIfCached()) {
                // We're forbidden from using the network and the cache is insufficient.
                return new CacheStrategy(null, null);
            }

            return candidate;
        }

        /**
         * Returns a strategy to use assuming the request can use the network.
         */
        private CacheStrategy getCandidate() {
            // No cached response.
            //todo 1、没有缓存,进行网络请求
            if (cacheResponse == null) {
                return new CacheStrategy(request, null);
            }

            //todo okhttp会保存ssl握手信息  Handshake ,如果这次发起了https请求，但是缓存的响应中没有握手信息，发起网络请求

            //todo 2、https请求，但是没有握手信息,进行网络请求
            //Drop the cached response if it's missing a required handshake.
            if (request.isHttps() && cacheResponse.handshake() == null) {
                return new CacheStrategy(request, null);
            }

            //todo 3、主要是通过响应码以及头部缓存控制字段判断响应能不能缓存，不能缓存那就进行网络请求
            //If this response shouldn't have been stored, it should never be used
            //as a response source. This check should be redundant as long as the
            //persistence store is well-behaved and the rules are constant.
            if (!isCacheable(cacheResponse, request)) {
                return new CacheStrategy(request, null);
            }

            CacheControl requestCaching = request.cacheControl();
            //todo 4、如果 请求包含：CacheControl:no-cache 需要与服务器验证缓存有效性
            // 或者请求头包含 If-Modified-Since：时间 值为lastModified或者data 如果服务器没有在该头部指定的时间之后修改了请求的数据，服务器返回304(无修改)
            // 或者请求头包含 If-None-Match：值就是Etag（资源标记）服务器将其与存在服务端的Etag值进行比较；如果匹配，返回304
            // 请求头中只要存在三者中任意一个，进行网络请求
            if (requestCaching.noCache() || hasConditions(request)) {
                return new CacheStrategy(request, null);
            }

            //todo 5、如果缓存响应中存在 Cache-Control:immutable 响应内容将一直不会改变,可以使用缓存
            CacheControl responseCaching = cacheResponse.cacheControl();
            if (responseCaching.immutable()) {
                return new CacheStrategy(null, cacheResponse);
            }

            //todo 6、根据 缓存响应的 控制缓存的响应头 判断是否允许使用缓存
            // 6.1、获得缓存的响应从创建到现在的时间
            long ageMillis = cacheResponseAge();
            //todo
            // 6.2、获取这个响应有效缓存的时长
            long freshMillis = computeFreshnessLifetime();
            if (requestCaching.maxAgeSeconds() != -1) {
                //todo 如果请求中指定了 max-age 表示指定了能拿的缓存有效时长，就需要综合响应有效缓存时长与请求能拿缓存的时长，获得最小的能够使用响应缓存的时长
                freshMillis = Math.min(freshMillis,
                        SECONDS.toMillis(requestCaching.maxAgeSeconds()));
            }
            //todo
            // 6.3 请求包含  Cache-Control:min-fresh=[秒]  能够使用还未过指定时间的缓存 （请求认为的缓存有效时间）
            long minFreshMillis = 0;
            if (requestCaching.minFreshSeconds() != -1) {
                minFreshMillis = SECONDS.toMillis(requestCaching.minFreshSeconds());
            }

            //todo
            // 6.4
            //  6.4.1、Cache-Control:must-revalidate 可缓存但必须再向源服务器进行确认
            //  6.4.2、Cache-Control:max-stale=[秒] 缓存过期后还能使用指定的时长  如果未指定多少秒，则表示无论过期多长时间都可以；如果指定了，则只要是指定时间内就能使用缓存
            // 前者会忽略后者，所以判断了不必须向服务器确认，再获得请求头中的max-stale
            long maxStaleMillis = 0;
            if (!responseCaching.mustRevalidate() && requestCaching.maxStaleSeconds() != -1) {
                maxStaleMillis = SECONDS.toMillis(requestCaching.maxStaleSeconds());
            }

            //todo
            // 6.5 不需要与服务器验证有效性 && 响应存在的时间+请求认为的缓存有效时间 小于 缓存有效时长+过期后还可以使用的时间
            // 允许使用缓存
            if (!responseCaching.noCache() && ageMillis + minFreshMillis < freshMillis + maxStaleMillis) {
                Response.Builder builder = cacheResponse.newBuilder();
                //todo 如果已过期，但未超过 过期后继续使用时长，那还可以继续使用，只用添加相应的头部字段
                if (ageMillis + minFreshMillis >= freshMillis) {
                    builder.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                }
                //todo 如果缓存已超过一天并且响应中没有设置过期时间也需要添加警告
                long oneDayMillis = 24 * 60 * 60 * 1000L;
                if (ageMillis > oneDayMillis && isFreshnessLifetimeHeuristic()) {
                    builder.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                }
                return new CacheStrategy(null, builder.build());
            }

            // Find a condition to add to the request. If the condition is satisfied, the
            // response body
            // will not be transmitted.
            //todo 7、缓存过期了
            String conditionName;
            String conditionValue;
            if (etag != null) {
                conditionName = "If-None-Match";
                conditionValue = etag;
            } else if (lastModified != null) {
                conditionName = "If-Modified-Since";
                conditionValue = lastModifiedString;
            } else if (servedDate != null) {
                conditionName = "If-Modified-Since";
                conditionValue = servedDateString;
            } else {
                return new CacheStrategy(request, null); // No condition! Make a regular request.
            }
            //todo 如果设置了 If-None-Match/If-Modified-Since 服务器是可能返回304(无修改)的,使用缓存的响应体
            Headers.Builder conditionalRequestHeaders = request.headers().newBuilder();
            Internal.instance.addLenient(conditionalRequestHeaders, conditionName, conditionValue);

            Request conditionalRequest = request.newBuilder()
                    .headers(conditionalRequestHeaders.build())
                    .build();
            return new CacheStrategy(conditionalRequest, cacheResponse);
        }

        /**
         * Returns the number of milliseconds that the response was fresh for, starting from the
         * served
         * date.
         */
        private long computeFreshnessLifetime() {
            CacheControl responseCaching = cacheResponse.cacheControl();
            /**
             * todo
             *  max-age：资源最大有效时间/秒
             *  expires:缓存过期的时间(服务器时间,可能客户端时间和服务器不一致)
             *    服务器如果发出了Data(服务器发出这个消息的时间) 则expires-data为有效时间
             *    没发出Data 则用 expires-收到响应的时间  为有效时间
             *  Last-Modified: 请求的数据最后修改的时间  进行试探性过期时间的计算
             *    服务器如果发出了Data(服务器发出这个消息的时间) 则data-lastModified 作为有效时间
             *    没发出Data 则用 对应缓存响应发起请求的时间-lastModified  作为有效时间
             */
            if (responseCaching.maxAgeSeconds() != -1) {
                return SECONDS.toMillis(responseCaching.maxAgeSeconds());
            } else if (expires != null) {
                long servedMillis = servedDate != null
                        ? servedDate.getTime()
                        : receivedResponseMillis;
                long delta = expires.getTime() - servedMillis;
                return delta > 0 ? delta : 0;
            } else if (lastModified != null
                    && cacheResponse.request().url().query() == null) {
                // As recommended by the HTTP RFC and implemented in Firefox, the
                // max age of a document should be defaulted to 10% of the
                // document's age at the time it was served. Default expiration
                // dates aren't used for URIs containing a query.
                long servedMillis = servedDate != null
                        ? servedDate.getTime()
                        : sentRequestMillis;
                long delta = servedMillis - lastModified.getTime();
                return delta > 0 ? (delta / 10) : 0;
            }
            return 0;
        }

        /**
         * Returns the current age of the response, in milliseconds. The calculation is specified
         * by RFC
         * 7234, 4.2.3 Calculating Age.
         */
        private long cacheResponseAge() {
            //todo
            // 响应Data字段：服务器发出这个消息的时间
            // 响应Age字段: 当代理服务器用自己缓存的实体去响应请求时，会用该头部表明该实体从产生到现在经过多长时间了
            // 计算收到响应的时间与服务器创建发出这个消息的时间差值:apparentReceivedAge
            // 取出apparentReceivedAge 与ageSeconds最大值并赋予receivedAge （代理服务器缓存的时间，意义如上的结果，但是要拿最大值）
            // 计算从发起请求到收到响应的时间差responseDuration
            // 计算现在与收到响应的时间差residentDuration
            // 三者加起来就是响应已存在的总时长
            long apparentReceivedAge = servedDate != null
                    ? Math.max(0, receivedResponseMillis - servedDate.getTime())
                    : 0;
            long receivedAge = ageSeconds != -1
                    ? Math.max(apparentReceivedAge, SECONDS.toMillis(ageSeconds))
                    : apparentReceivedAge;
            long responseDuration = receivedResponseMillis - sentRequestMillis;
            long residentDuration = nowMillis - receivedResponseMillis;
            return receivedAge + responseDuration + residentDuration;
        }

        /**
         * Returns true if computeFreshnessLifetime used a heuristic. If we used a heuristic to
         * serve a
         * cached response older than 24 hours, we are required to attach a warning.
         */
        private boolean isFreshnessLifetimeHeuristic() {
            return cacheResponse.cacheControl().maxAgeSeconds() == -1 && expires == null;
        }

        /**
         * Returns true if the request contains conditions that save the server from sending a
         * response
         * that the client has locally. When a request is enqueued with its own conditions, the
         * built-in
         * response cache won't be used.
         */
        private static boolean hasConditions(Request request) {
            return request.header("If-Modified-Since") != null || request.header("If-None-Match") != null;
        }
    }
}
