package com.xiangxue.webview.remotewebview.webviewclient;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.xiangxue.webview.R;
import com.xiangxue.webview.remotewebview.callback.WebViewCallBack;

import java.util.Map;

import static com.xiangxue.webview.remotewebview.BaseWebView.CONTENT_SCHEME;

public class XiangxueWebviewClient extends WebViewClient {

    private static final String TAG = "XXWebviewCallBack";
    public static final String SCHEME_SMS = "sms:";
    private WebViewCallBack webViewCallBack;
    private WebView webView;
    boolean isReady;
    private Map<String, String> mHeaders;
    private WebviewTouch mWebviewTouch;

    public XiangxueWebviewClient(WebView webView, WebViewCallBack webViewCallBack, Map<String, String> headers, WebviewTouch touch){
        this.webViewCallBack = webViewCallBack;
        this.webView = webView;
        this.mHeaders = headers;
        this.mWebviewTouch = touch;
    }

    public boolean isReady(){
        return this.isReady;
    }

    public interface WebviewTouch{
        boolean isTouchByUser();
    }

    /**
     * url重定向会执行此方法以及点击页面某些链接也会执行此方法
     *
     * @return true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载 false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e(TAG, "shouldOverrideUrlLoading url: " + url);
        // 当前链接的重定向, 通过是否发生过点击行为来判断
        if (!mWebviewTouch.isTouchByUser()) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        // 如果链接跟当前链接一样，表示刷新
        if (webView.getUrl().equals(url)) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        if (handleLinked(url)) {
            return true;
        }
        // 控制页面中点开新的链接在当前webView中打开
        view.loadUrl(url, mHeaders);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.e(TAG, "shouldOverrideUrlLoading url: "+ request.getUrl());
        // 当前链接的重定向
        if (!mWebviewTouch.isTouchByUser()) {
            return super.shouldOverrideUrlLoading(view, request);
        }
        // 如果链接跟当前链接一样，表示刷新
        if (webView.getUrl().equals(request.getUrl().toString())) {
            return super.shouldOverrideUrlLoading(view, request);
        }
        if (handleLinked(request.getUrl().toString())) {
            return true;
        }
        // 控制页面中点开新的链接在当前webView中打开
        view.loadUrl(request.getUrl().toString(), mHeaders);
        return true;
    }

    /**
     * 支持电话、短信、邮件、地图跳转，跳转的都是手机系统自带的应用
     */
    private boolean handleLinked(String url) {
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(WebView.SCHEME_MAILTO)
                || url.startsWith(WebView.SCHEME_GEO)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                webView.getContext().startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.e(TAG, "onPageFinished url:" + url);
        if (!TextUtils.isEmpty(url) && url.startsWith(CONTENT_SCHEME)) {
            isReady = true;
        }
        if (webViewCallBack != null) {
            webViewCallBack.pageFinished(url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.e(TAG, "onPageStarted url: " + url);
        if (webViewCallBack != null) {
            webViewCallBack.pageStarted(url);
        }
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    @TargetApi(21)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return shouldInterceptRequest(view, request.getUrl().toString());
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return null;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e(TAG, "webview error" + errorCode + " + " + description);
        if (webViewCallBack != null) {
            webViewCallBack.onError();
        }
    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        String channel = "";
        if (!TextUtils.isEmpty(channel) && channel.equals("play.google.com")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
            String message = webView.getContext().getString(R.string.ssl_error);
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = webView.getContext().getString(R.string.ssl_error_not_trust);
                    break;
                case SslError.SSL_EXPIRED:
                    message = webView.getContext().getString(R.string.ssl_error_expired);
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = webView.getContext().getString(R.string.ssl_error_mismatch);
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = webView.getContext().getString(R.string.ssl_error_not_valid);
                    break;
            }
            message += webView.getContext().getString(R.string.ssl_error_continue_open);

            builder.setTitle(R.string.ssl_error);
            builder.setMessage(message);
            builder.setPositiveButton(R.string.continue_open, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            handler.proceed();
        }
    }
}