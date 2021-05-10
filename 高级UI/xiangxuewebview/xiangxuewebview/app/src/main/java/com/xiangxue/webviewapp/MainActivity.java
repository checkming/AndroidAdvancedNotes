package com.xiangxue.webviewapp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.arch.demo.core.baseservices.ILoginListener;
import com.arch.demo.core.baseservices.ILoginService;
import com.limpoxe.support.servicemanager.ServiceManager;
import com.xiangxue.webview.command.CommandsManager;
import com.xiangxue.webview.utils.AidlError;
import com.xiangxue.webview.command.Command;
import com.xiangxue.webview.command.ResultBack;
import com.xiangxue.webview.utils.WebConstants;
import com.xiangxue.webview.remotewebview.BaseWebView;

import java.util.HashMap;
import java.util.Map;

import static com.xiangxue.common.router.AppRouter.APP_LOGIN_ACTIVITY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.openWeb1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startCommonWeb(MainActivity.this, "腾讯网", "https://xw.qq.com/?f=qqcom", WebConstants.LEVEL_BASE);
            }
        });

        CommandsManager.getInstance().registerCommand(WebConstants.LEVEL_ACCOUNT, appDataProviderCommand);
        CommandsManager.getInstance().registerCommand(WebConstants.LEVEL_BASE, pageRouterCommand);
        CommandsManager.getInstance().registerCommand(WebConstants.LEVEL_ACCOUNT, appLoginCommand);
        findViewById(R.id.openWeb2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startCommonWeb(MainActivity.this, "AIDL测试", BaseWebView.CONTENT_SCHEME + "aidl.html", WebConstants.LEVEL_ACCOUNT);
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
                WebActivity.startAccountWeb(MainActivity.this, "百度", "http://www.baidu.com", WebConstants.LEVEL_ACCOUNT, accountInfo);
            }
        });

    }


    /**
     * 页面路由
     */
    private final Command pageRouterCommand = new Command() {


        @Override
        public String name() {
            return "newPage";
        }

        @Override
        public void exec(Context context, Map params, ResultBack resultBack) {
            String newUrl = params.get("url").toString();
            String title = (String) params.get("title");
        }
    };



    /**
     * 页面路由
     */
    private final Command appLoginCommand = new Command() {
        Map params;
        ResultBack resultBack;
        private ILoginListener loginListener = new ILoginListener() {
            @Override
            public void onLogin() {
                try {
                    String callbackName = "";
                    if (params.get("type") == null) {
                        AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                        resultBack.onResult(WebConstants.FAILED, name(), aidlError);
                        return;
                    }
                    if (params.get(WebConstants.WEB2NATIVE_CALLBACk) != null) {
                        callbackName = params.get(WebConstants.WEB2NATIVE_CALLBACk).toString();
                    }
                    String type = params.get("type").toString();
                    HashMap map = new HashMap();
                    switch (type) {
                        case "account":
                            map.put("accountId", "test123456");
                            map.put("accountName", "xiangxue");
                            break;
                    }
                    if (!TextUtils.isEmpty(callbackName)) {
                        map.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName);
                    }
                    resultBack.onResult(WebConstants.SUCCESS, name(), map);
                } catch (Exception e) {
                    e.printStackTrace();
                    AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                    resultBack.onResult(WebConstants.FAILED, name(), aidlError);
                }
            }

            @Override
            public void onLogout() {

            }

            @Override
            public void onRegister() {

            }

            @Override
            public void onCancel() {

            }
        };

        @Override
        public String name() {
            return "appLogin";
        }

        @Override
        public void exec(Context context, Map params, ResultBack resultBack) {
            ILoginService iLoginService = (ILoginService) ServiceManager.getService(ILoginService.LOGIN_SERVICE_NAME);
            if(!iLoginService.isLogin()){
                iLoginService.registerLoginListener(loginListener);
                iLoginService.login();
            }
            this.params = params;
            this.resultBack = resultBack;
        }
    };

    // 获取native data
    private final Command appDataProviderCommand = new Command() {
        @Override
        public String name() {
            return "appDataProvider";
        }

        @Override
        public void exec(Context context, Map params, ResultBack resultBack) {
            try {
                String callbackName = "";
                if (params.get("type") == null) {
                    AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                    resultBack.onResult(WebConstants.FAILED, this.name(), aidlError);
                    return;
                }
                if (params.get(WebConstants.WEB2NATIVE_CALLBACk) != null) {
                    callbackName = params.get(WebConstants.WEB2NATIVE_CALLBACk).toString();
                }
                String type = params.get("type").toString();
                HashMap map = new HashMap();
                switch (type) {
                    case "account":
                        map.put("accountId", "test123456");
                        map.put("accountName", "xiangxue");
                        break;
                }
                if (!TextUtils.isEmpty(callbackName)) {
                    map.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName);
                }
                resultBack.onResult(WebConstants.SUCCESS, this.name(), map);
            } catch (Exception e) {
                e.printStackTrace();
                AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                resultBack.onResult(WebConstants.FAILED, this.name(), aidlError);
            }
        }
    };
}
