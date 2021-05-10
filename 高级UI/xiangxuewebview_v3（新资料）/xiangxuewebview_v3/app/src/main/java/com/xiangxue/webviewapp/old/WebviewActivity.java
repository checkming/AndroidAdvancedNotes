package com.xiangxue.webviewapp.old;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.xiangxue.webviewapp.BuildConfig;
import com.xiangxue.webviewapp.R;
import com.xiangxue.webviewapp.TestActivityB;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

public class WebviewActivity extends AppCompatActivity {
    public static final String URLKEY="url";
    public static final String QUERYURL="query";
    public static final String ASSETS="http://assets";
    public static final String PREFGROUP="main";
    public static final String FORCEPREF="force";
    
    private WebView mWebView;
    private String queryUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            queryUrl = b.getString(URLKEY);
        }
        //getSupportActionBar().hide();
        setContentView(R.layout.fragment_common_webview);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            try {
                Method m = WebView.class.getDeclaredMethod("setWebContentsDebuggingEnabled", boolean.class);
                m.setAccessible(true);
                m.invoke(mWebView, true);
            } catch (Exception e) {
            }
        }
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.startsWith(ASSETS)) {
                    String remain = url.substring(ASSETS.length() + 1);
                    if (remain.startsWith(QUERYURL)) {
                        try {
                            URL cu = new URL(queryUrl);
                            URLConnection con = cu.openConnection();
                            InputStream is = con.getInputStream();
                            return new WebResourceResponse(null, null, is);
                        } catch (Exception e) {
                            Log.e("HTTP", "unable to query " + queryUrl + ": " + e);
                            return new WebResourceResponse(null, null, 404, e.getLocalizedMessage(), null, null);
                        }
                    } else {
                        try {
                            InputStream is = getAssets().open(remain);
                            return new WebResourceResponse(null, null, is);
                        } catch (IOException e) {
                            Log.e("HTTP", "unable to load asset " + remain + ": " + e);
                            return null;
                        }
                    }
                } else {
                    return super.shouldInterceptRequest(view, url);
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //we fake some http url to allow xml http requests
        mWebView.loadUrl(ASSETS + "/index.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webview_activity_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this, TestActivityB.class);
        i.putExtra(FORCEPREF, true);
        startActivity(i);
        finish();
        return true;
    }
}