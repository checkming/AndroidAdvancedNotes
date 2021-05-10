package com.xiangxue.webview.remotewebview.javascriptinterface;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

/**
 *
 * 1. 保留command的注册
 * 2. 不支持command通过远程aidl方式调用
 */
public final class WebviewJavascriptInterface {

    private final Context mContext;
    private final Handler mHandler = new Handler();
    private JavascriptCommand javascriptCommand;

    public WebviewJavascriptInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void post(final String cmd, final String param) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (javascriptCommand != null) {
                        javascriptCommand.exec(mContext, cmd, param);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setJavascriptCommand(JavascriptCommand javascriptCommand) {
        this.javascriptCommand = javascriptCommand;
    }

    public interface JavascriptCommand {
        void exec(Context context, String cmd, String params);
    }
}
