package com.xiangxue.webview.remotewebview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.webkit.WebView;

import com.google.gson.Gson;

import com.xiangxue.webview.remotewebview.callback.WebViewCallBack;
import com.xiangxue.webview.remotewebview.javascriptinterface.WebviewJavascriptInterface;
import com.xiangxue.webview.remotewebview.settings.WebviewDefaultSetting;
import com.xiangxue.webview.remotewebview.webviewclient.XiangxueWebviewClient;

import java.util.HashMap;
import java.util.Map;

public class BaseWebView extends WebView implements XiangxueWebviewClient.WebviewTouch {
    private static final String TAG = "XiangxueWebView";
    public static final String CONTENT_SCHEME = "file:///android_asset/";
    private ActionMode.Callback mCustomCallback;
    protected Context context;
    private WebViewCallBack webViewCallBack;
    private Map<String, String> mHeaders;
    private WebviewJavascriptInterface remoteInterface = null;
    private XiangxueWebviewClient mXiangxueWebviewClient;

    public BaseWebView(Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public WebViewCallBack getWebViewCallBack(){
        return webViewCallBack;
    }

    public void registerdWebViewCallBack(WebViewCallBack webViewCallBack) {
        this.webViewCallBack = webViewCallBack;
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    protected void init(Context context) {
        this.context = context;
        WebviewDefaultSetting.getInstance().toSetting(this);
        mXiangxueWebviewClient = new XiangxueWebviewClient(this, webViewCallBack, mHeaders, this);
        setWebViewClient(mXiangxueWebviewClient);

        /**
         * Web Native交互触发
         */
        if (remoteInterface == null) {
            remoteInterface = new WebviewJavascriptInterface(context);
            remoteInterface.setJavascriptCommand(new WebviewJavascriptInterface.JavascriptCommand() {
                @Override
                public void exec(Context context, String cmd, String params) {
                    if (webViewCallBack != null) {
                        webViewCallBack.exec(context, webViewCallBack.getCommandLevel(), cmd, params, BaseWebView.this);
                    }
                }
            });
        }
        addJavascriptInterface(remoteInterface, "webview");
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        final ViewParent parent = getParent();
        if (parent != null) {
            return parent.startActionModeForChild(this, wrapCallback(callback));
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        final ViewParent parent = getParent();
        if (parent != null) {
            return parent.startActionModeForChild(this, wrapCallback(callback), type);
        }
        return null;
    }

    private ActionMode.Callback wrapCallback(ActionMode.Callback callback) {
        if (mCustomCallback != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return new CallbackWrapperM(mCustomCallback, callback);
            } else {
                return new CallbackWrapperBase(mCustomCallback, callback);
            }
        }
        return callback;
    }

    public void setCustomActionCallback(ActionMode.Callback callback) {
        mCustomCallback = callback;
    }

    private static class CallbackWrapperBase implements ActionMode.Callback {
        private final ActionMode.Callback mWrappedCustomCallback;
        private final ActionMode.Callback mWrappedSystemCallback;

        public CallbackWrapperBase(ActionMode.Callback customCallback, ActionMode.Callback systemCallback) {
            mWrappedCustomCallback = customCallback;
            mWrappedSystemCallback = systemCallback;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return mWrappedCustomCallback.onCreateActionMode(mode, menu);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return mWrappedCustomCallback.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return mWrappedCustomCallback.onActionItemClicked(mode, item);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            try {
                mWrappedCustomCallback.onDestroyActionMode(mode);
                mWrappedSystemCallback.onDestroyActionMode(mode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static class CallbackWrapperM extends ActionMode.Callback2 {
        private final ActionMode.Callback mWrappedCustomCallback;
        private final ActionMode.Callback mWrappedSystemCallback;

        public CallbackWrapperM(ActionMode.Callback customCallback, ActionMode.Callback systemCallback) {
            mWrappedCustomCallback = customCallback;
            mWrappedSystemCallback = systemCallback;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return mWrappedCustomCallback.onCreateActionMode(mode, menu);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return mWrappedCustomCallback.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return mWrappedCustomCallback.onActionItemClicked(mode, item);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mWrappedCustomCallback.onDestroyActionMode(mode);
            mWrappedSystemCallback.onDestroyActionMode(mode);
        }

        @Override
        public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
            if (mWrappedCustomCallback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) mWrappedCustomCallback).onGetContentRect(mode, view, outRect);
            } else if (mWrappedSystemCallback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) mWrappedSystemCallback).onGetContentRect(mode, view, outRect);
            } else {
                super.onGetContentRect(mode, view, outRect);
            }
        }
    }

    public void setContent(String htmlContent) {
        try {
            loadDataWithBaseURL(CONTENT_SCHEME, htmlContent, "text/html", "UTF-8", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadUrl(String url) {
        if (mHeaders == null) {
            super.loadUrl(url);
        } else {
            super.loadUrl(url, mHeaders);
        }
        Log.e(TAG, "DWebView load url: " + url);
        resetAllStateInternal(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        Log.e(TAG, "DWebView load url: " + url);
        resetAllStateInternal(url);
    }

    public void handleCallback(String response) {
        if (!TextUtils.isEmpty(response)) {
            String trigger = "javascript:" + "dj.callback" + "(" + response + ")";
            evaluateJavascript(trigger, null);
        }
    }

    public void loadJS(String cmd, Object param) {
        String trigger = "javascript:" + cmd + "(" + new Gson().toJson(param) + ")";
        evaluateJavascript(trigger, null);
    }

    public void dispatchEvent(String name) {
        Map<String, String> param = new HashMap<>(1);
        param.put("name", name);
        loadJS("dj.dispatchEvent", param);
    }

    private boolean mTouchByUser;

    public boolean isTouchByUser() {
        return mTouchByUser;
    }

    private void resetAllStateInternal(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return;
        }
        resetAllState();
    }

    // 加载url时重置touch状态
    protected void resetAllState() {
        mTouchByUser = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchByUser = true;
                break;
        }
        return super.onTouchEvent(event);
    }
}