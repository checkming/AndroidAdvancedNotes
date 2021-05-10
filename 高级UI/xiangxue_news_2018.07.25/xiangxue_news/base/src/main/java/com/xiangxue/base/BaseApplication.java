package com.xiangxue.base;

import android.app.Application;

public class BaseApplication extends Application {
    // OOM won't happen.
    public static Application sApplication;
    public static boolean sDebug;

    public void setDebug(boolean isDebug){
        sDebug = isDebug;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        sApplication = this;
    }

}
