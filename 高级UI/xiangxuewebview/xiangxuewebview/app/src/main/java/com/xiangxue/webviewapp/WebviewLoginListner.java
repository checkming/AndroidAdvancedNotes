package com.xiangxue.webviewapp;

import com.arch.demo.core.baseservices.ILoginListener;
import com.arch.demo.core.baseservices.ILoginService;
import com.limpoxe.support.servicemanager.ServiceManager;

import java.util.HashMap;
import java.util.Map;

public class WebviewLoginListner extends ILoginListener {
    private Map<String, Object> mParams;
    private static ILoginService mLoginService = (ILoginService) ServiceManager.getService(ILoginService.LOGIN_SERVICE_NAME);

    public WebviewLoginListner(Map<String, Object> params) {
        mParams = params;
    }

    @Override
    public void onLogin() {
        jsCallLogin(mParams);
        mLoginService.unregisterListener(this);
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

    /**
     * 调用js方法，讲参数信息传递给h5
     */
    private void jsCallLogin(Map<String, Object> params) {

    }
}
