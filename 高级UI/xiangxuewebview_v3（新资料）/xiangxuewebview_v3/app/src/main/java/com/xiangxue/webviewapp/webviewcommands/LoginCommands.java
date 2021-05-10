package com.xiangxue.webviewapp.webviewcommands;

import android.text.TextUtils;

import com.arch.demo.core.baseservices.ILoginListener;
import com.arch.demo.core.baseservices.ILoginService;
import com.arch.demo.core.preference.PreferencesUtil;
import com.arch.demo.core.utils.GsonUtils;
import com.xiangxue.webview.ICallbackFromMainToWeb;
import com.xiangxue.webview.command.Command;
import com.xiangxue.webview.mainprocess.CommandsManager;
import com.xiangxue.webview.utils.AidlError;
import com.xiangxue.webview.utils.WebConstants;
import com.xiangxue.webviewapp.router.AppRouter;
import com.xiangxue.webviewapp.router.RouteServiceManager;

import java.util.HashMap;
import java.util.Map;

public final class LoginCommands {

    private LoginCommands() {
    }

    public static final void init() {
        CommandsManager.getsInstance().registerCommand(appLoginCommand);
    }

    /**
     * 页面路由
     */
    private static final Command appLoginCommand = new Command() {
        Map params;
        ICallbackFromMainToWeb resultBack;
        private ILoginListener loginListener = new ILoginListener() {
            @Override
            public void onLogin() {
                try {
                    String callbackName = "";
                    if (params.get("type") == null) {
                        AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                        resultBack.onResult(WebConstants.FAILED, name(), aidlError.toString());
                        return;
                    }
                    if (params.get(WebConstants.WEB2NATIVE_CALLBACk) != null) {
                        callbackName = params.get(WebConstants.WEB2NATIVE_CALLBACk).toString();
                    }
                    String type = params.get("type").toString();
                    HashMap map = new HashMap();
                    switch (type) {
                        case "account":
                            map.put("accountName", PreferencesUtil.getInstance().getString("UserName"));
                            break;
                    }
                    if (!TextUtils.isEmpty(callbackName)) {
                        map.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName);
                    }
                    resultBack.onResult(WebConstants.SUCCESS, name(),  GsonUtils.toJson(map));
                } catch (Exception e) {
                    e.printStackTrace();
                    AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                    //resultBack.onResult(WebConstants.FAILED, name(), aidlError.toString());
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
        public void exec(Map params, ICallbackFromMainToWeb resultBack) {
            ILoginService iLoginService = RouteServiceManager.provide(ILoginService.class, AppRouter.APP_LOGIN_SERVICE);
            if (!iLoginService.isLogin()) {
                iLoginService.registerLoginListener(loginListener);
                iLoginService.login();
            }
            this.params = params;
            this.resultBack = resultBack;
        }
    };
}
