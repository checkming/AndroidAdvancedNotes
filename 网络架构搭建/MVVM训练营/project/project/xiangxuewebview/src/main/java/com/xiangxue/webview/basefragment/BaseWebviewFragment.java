package com.xiangxue.webview.basefragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.xiangxue.webview.R;
import com.xiangxue.webview.basefragment.commands.ShowDialogCommand;
import com.xiangxue.webview.basefragment.commands.ToastCommand;
import com.xiangxue.webview.command.CommandsManager;
import com.xiangxue.webview.remotewebview.BaseWebView;
import com.xiangxue.webview.remotewebview.callback.WebViewCallBack;
import com.xiangxue.webview.utils.LoadingCallback;
import com.xiangxue.webview.utils.WebConstants;

import java.util.HashMap;

public abstract class BaseWebviewFragment extends BaseFragment implements WebViewCallBack {
    public static final String ACCOUNT_INFO_HEADERS = "account_header";
    protected BaseWebView webView;
    protected HashMap<String, String> accountInfoHeaders;
    public static final int REQUEST_CODE_LOLIPOP = 1;

    public String webUrl;
    LoadService loadService;
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

        CommandsManager.getInstance().registerCommand(new ToastCommand());
        CommandsManager.getInstance().registerCommand(new ShowDialogCommand());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        webView = view.findViewById(R.id.web_view);
        if(accountInfoHeaders != null) {
            webView.setHeaders(accountInfoHeaders);
        }
        LoadSir.beginBuilder()
                .addCallback(new LoadingCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
        loadService = LoadSir.getDefault().register(webView, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                // your retry logic
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.registerdWebViewCallBack(this);
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
    public void pageStarted(String url) {
        loadService.showCallback(LoadingCallback.class);
    }

    @Override
    public void pageFinished(String url) {
        loadService.showSuccess();
    }

    @Override
    public void onError() {
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

    @Override
    public void onShowFileChooser(Intent cameraIntent, ValueCallback<Uri[]> filePathCallback) {
        //整个弹出框为:相机、相册、文件管理
        //如果安装了其他的相机、文件管理程序，也有可能会弹出
        //selectionIntent(相册、文件管理)
        //Intent selectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //selectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        //selectionIntent.setType("image/*");
        mFilePathCallback = filePathCallback;
        //------------------------------------
        //如果通过下面的方式，则弹出的选择框有:相机、相册(Android9.0,Android8.0)
        //如果是小米Android6.0系统上，依然是：相机、相册、文件管理
        //如果安装了其他的相机(百度魔拍)、文件管理程序(ES文件管理器)，也有可能会弹出
        Intent selectionIntent = new Intent(Intent.ACTION_PICK,null);
        selectionIntent.setType("image/*");
        //------------------------------------

        Intent[] intentArray;
        if (cameraIntent != null) {
            intentArray = new Intent[]{cameraIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.file_chooser));
        chooserIntent.putExtra(Intent.EXTRA_INTENT, selectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);

    }

    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_LOLIPOP:
                Uri[] results = null;
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            Log.d("AppChooserFragment", mCameraPhotoPath);

                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
        }
    }
}
