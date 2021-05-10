package com.xiangxue.webviewapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.xiangxue.webview.WebviewActivity;
import com.xiangxue.webview.BaseWebView;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.openWeb1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.startCommonWeb(MainActivity.this, "腾讯网", "https://xw.qq.com/?f=qqcom");
            }
        });

        findViewById(R.id.openWeb2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.startCommonWeb(MainActivity.this, "AIDL测试", BaseWebView.CONTENT_SCHEME + "aidl.html");
            }
        });

        findViewById(R.id.openWeb3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for account level
                HashMap<String, String> accountInfo = new HashMap<>();
                accountInfo.put("username", "TestAccount");
                accountInfo.put("access_token", "880fed4ca2aabd20ae9a5dd774711de2");
                accountInfo.put("phone", "+8613989898898");
                WebviewActivity.startCommonWeb(MainActivity.this, "百度", "http://www.baidu.com");
            }
        });
        findViewById(R.id.alert_issue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.startCommonWeb(MainActivity.this, "Alert问题", BaseWebView.CONTENT_SCHEME + "alert_issue.html");
            }
        });

        findViewById(R.id.auto_zoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.startCommonWeb(MainActivity.this, "自适应屏幕问题", "http://www.customs.gov.cn/customs/302249/302266/302267/2491213/index.html");
            }
        });

        findViewById(R.id.webview_pre_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.startCommonWeb(MainActivity.this, "腾讯网", "https://xw.qq.com/?f=qqcom");
            }
        });


        findViewById(R.id.file_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.startCommonWeb(MainActivity.this, "文件上传", "file:///android_asset/www/index.html");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
