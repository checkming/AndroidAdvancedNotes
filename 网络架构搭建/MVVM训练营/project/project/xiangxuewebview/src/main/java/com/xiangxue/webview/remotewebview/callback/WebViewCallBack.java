package com.xiangxue.webview.remotewebview.callback;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;

/**
 * WebView回调统一处理
 * 所有涉及到WebView交互的都必须实现这个callback
 */
public interface WebViewCallBack {

    void pageStarted(String url);

    void pageFinished(String url);

    void onError();

    void onShowFileChooser(Intent cameraIntent, ValueCallback<Uri[]> filePathCallback);
}
