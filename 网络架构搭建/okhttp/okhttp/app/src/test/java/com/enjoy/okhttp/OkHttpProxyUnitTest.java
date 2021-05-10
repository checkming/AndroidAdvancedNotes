package com.enjoy.okhttp;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OkHttpProxyUnitTest {

    OkHttpClient socksClient =
            new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(
                    "localhost", 808))).build();

    OkHttpClient httpClient =
            new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    "114.239.145.90", 808)))
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            //.....
                            Response proceed = chain.proceed(chain.request());
                            //......
                            return proceed;
                        }
                    })
                    .build();


    @Test
    public void testOkHttpHttpProxy() throws IOException {
        Request request = new Request.Builder()
                .url("http://restapi.amap.com/v3/weather/weatherInfo?city=长沙&key" +
                        "=13cb58f5884f9749287abbead9c658f2")
                .build();
        //执行同步请求
        Call call = httpClient.newCall(request);
        Response response = call.execute();

        //获得响应
        ResponseBody body = response.body();
        System.out.println("http代理 响应==》" + body.string());

    }


    @Test
    public void testOkHttpSocksProxy() throws IOException {
        //启动 socks代理
        new Thread() {
            @Override
            public void run() {
                try {
                    SocksProxy.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Request request = new Request.Builder()
                .url("http://restapi.amap.com/v3/weather/weatherInfo?city=长沙&key" +
                        "=13cb58f5884f9749287abbead9c658f2")
                .build();
        //执行同步请求
        Call call = socksClient.newCall(request);
        Response response = call.execute();

        //获得响应
        ResponseBody body = response.body();
        System.out.println("socks代理 响应==》" + body.string());

    }


}