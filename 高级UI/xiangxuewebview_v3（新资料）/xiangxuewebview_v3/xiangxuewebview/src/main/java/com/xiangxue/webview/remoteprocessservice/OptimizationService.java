package com.xiangxue.webview.remoteprocessservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.xiangxue.webview.BaseWebView;

/* This service is only used for performance optimization */
public class OptimizationService extends Service {
    /**
     * 本地Webview初始化都要不少时间, 首次初始化webview与第二次初始化不同，
     * 首次会比第二次慢很多。原因预计是webview首次初始化后，即使 webview
     * 已经释放，但一些webview 共用的全局服务或资源对象仍没有释放，第二次初
     * 始化时不需要再生成这些对象从而变快。我们可以在Application预先初始化
     * 好WebView, 当第二次初始化WebView的时候速度就快多了, 或者直接将其拿
     * 来使用。
     */
    BaseWebView webView;
    @Nullable
    @Override
    public void onCreate(){
        super.onCreate();
        webView = new BaseWebView(this);
        webView.loadUrl("https://xw.qq.com/?f=qqcom");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
