package com.arch.demo.core.baseservices;

import android.content.Context;

public interface ILoginService {

     String LOGIN_SERVICE_NAME = "login_service";
    /**
     * 检查是否登录
     *
     * @param needLogin 如果该值传入为true，则在当前未登录的时候会执行登录操作
     * @return 返回是否登录
     */
     boolean checkLogin(boolean needLogin);

     boolean isLogin();

     void checkUserInfo();

     String getToken();

     String getUUID();

     void refreshUserDetailInfo();

     void login();

     void login(boolean isMainAccountLogin);

     void logout();

     UserInfo getUserInfo();
    
     void onLoginCancel();

     void unregisterListener(ILoginListener listener);

     void registerLoginListener(ILoginListener listener);

     void thirdUnbind(ThirdAccountType accountType, Context context);

     void onTokenExpire();
    
     void onOpenAccountSuccess();

    /**
     * 检查开户的状态，当前状态是否可以开户
     */
     OpenAccountType checkOpenAccount();

    /**
     * 没有密码->没有绑定手机/邮箱->绑定手机/邮箱->设置密码
     * 绑定了手机/邮箱->设置密码
     */
     void startOpenAccount();

     enum ThirdAccountType {
        THIRD_TYPE_NONE(0),
        THIRD_TYPE_XIAOMI(1),
        THIRD_TYPE_WECHAT(2),
        THIRD_TYPE_WEIBO(3),
        THIRD_TYPE_FACEBOOK(4),
        THIRD_TYPE_GOOGLE(5),
        THIRD_TYPE_TWITTER(6);
        private final int mType;

        ThirdAccountType(int type) {
            this.mType = type;
        }

         int getThirdAccountType() {
            return mType;
        }
    }

     enum OpenAccountType {
        OPEN_ACCOUNT_NOT_LOGION(-1),
        OPEN_ACCOUNT_OK(0),
        OPEN_ACCOUNT_BIND(1),
        OPEN_ACCOUNT_SET_PWD(2);
         final int type;

        OpenAccountType(int type) {
            this.type = type;
        }

         int getType() {
            return type;
        }
    }
}
