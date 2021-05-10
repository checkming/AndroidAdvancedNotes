package com.xiangxue.webview.basefragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiangxue.webview.R;
import com.xiangxue.webview.remotewebview.callback.WebViewCallBack;
import com.xiangxue.webview.utils.WebConstants;
import com.xiangxue.webview.remotewebview.BaseWebView;

import java.util.HashMap;

public abstract class BaseWebviewFragment extends BaseFragment implements WebViewCallBack {
    public static final String ACCOUNT_INFO_HEADERS = "account_header";
    protected BaseWebView webView;
    protected HashMap<String, String> accountInfoHeaders;

    public String webUrl;

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            webUrl = bundle.getString(WebConstants.INTENT_TAG_URL);
            if(bundle.containsKey(ACCOUNT_INFO_HEADERS)){
                accountInfoHeaders = (HashMap<String, String>) bundle.getSerializable(ACCOUNT_INFO_HEADERS);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        webView = view.findViewById(R.id.web_view);
        if(accountInfoHeaders != null) {
            webView.setHeaders(accountInfoHeaders);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.registerdWebViewCallBack(this);
        CommandDispatcher.getInstance().initAidlConnect(getContext());
        loadUrl();
    }

    protected void loadUrl() {
        webView.loadUrl(webUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.dispatchEvent("pageResume");
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.dispatchEvent("pagePause");
        webView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        webView.dispatchEvent("pageStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.dispatchEvent("pageDestroy");
        clearWebView(webView);
    }


    @Override
    public int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }

    @Override
    public void pageStarted(String url) {

    }

    @Override
    public void pageFinished(String url) {

    }

    @Override
    public boolean overrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onError() {

    }

    @Override
    public void exec(Context context, int commandLevel, String cmd, String params, WebView webView) {
        CommandDispatcher.getInstance().exec(context, commandLevel, cmd, params, webView, getDispatcherCallBack());
    }

    protected CommandDispatcher.DispatcherCallBack getDispatcherCallBack() {
        return null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return onBackHandle();
        }
        return false;
    }

    protected boolean onBackHandle() {
        if (webView != null) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void clearWebView(WebView m) {
        if (m == null)
            return;
        if (Looper.myLooper() != Looper.getMainLooper())
            return;
        m.stopLoading();
        if (m.getHandler() != null) {
            m.getHandler().removeCallbacksAndMessages(null);
        }
        m.removeAllViews();
        ViewGroup mViewGroup = null;
        if ((mViewGroup = ((ViewGroup) m.getParent())) != null) {
            mViewGroup.removeView(m);
        }
        m.setWebChromeClient(null);
        m.setWebViewClient(null);
        m.setTag(null);
        m.clearHistory();
        m.destroy();
        m = null;
    }
}
