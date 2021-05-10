package com.arch.demo.core;

import android.app.Application;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class BaseApplication extends Application {

    public static BaseApplication sApplication;

    public static boolean isIsDebug() {
        return sIsDebug;
    }

    public static void setIsDebug(boolean sIsDebug) {
        BaseApplication.sIsDebug = sIsDebug;
    }

    private static boolean sIsDebug;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
