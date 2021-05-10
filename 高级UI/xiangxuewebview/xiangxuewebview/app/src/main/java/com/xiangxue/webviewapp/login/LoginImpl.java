package com.xiangxue.webviewapp.login;

import android.content.Context;


import com.alibaba.android.arouter.launcher.ARouter;
import com.arch.demo.core.baseservices.ILoginListener;
import com.arch.demo.core.baseservices.ILoginService;
import com.arch.demo.core.baseservices.UserInfo;
import com.xiangxue.common.router.AppRouter;

import java.util.ArrayList;
import java.util.List;

public class LoginImpl implements ILoginService {

    private List<ILoginListener> mLoginListeners = new ArrayList<>();

    @Override
    public boolean checkLogin(boolean needLogin) {
        return false;
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void checkUserInfo() {

    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getUUID() {
        return null;
    }

    @Override
    public void refreshUserDetailInfo() {
        for(ILoginListener loginListener:mLoginListeners){
            loginListener.onLogin();
        }
    }

    @Override
    public void login() {
        ARouter.getInstance().build(AppRouter.APP_LOGIN_ACTIVITY).navigation();
    }

    @Override
    public void login(boolean isMainAccountLogin) {

    }

    @Override
    public void logout() {

    }

    @Override
    public UserInfo getUserInfo() {
        return null;
    }

    @Override
    public void onLoginCancel() {

    }

    @Override
    public void unregisterListener(ILoginListener listener) {
        for(int i = 0; i < mLoginListeners.size(); i ++){
            if(mLoginListeners.get(i) == listener){
                mLoginListeners.remove(i);
            }
        }
    }

    @Override
    public void registerLoginListener(ILoginListener listener) {
        mLoginListeners.add(listener);
    }

    @Override
    public void thirdUnbind(ThirdAccountType accountType, Context context) {

    }

    @Override
    public void onTokenExpire() {

    }

    @Override
    public void onOpenAccountSuccess() {

    }

    @Override
    public OpenAccountType checkOpenAccount() {
        return null;
    }

    @Override
    public void startOpenAccount() {

    }
}
