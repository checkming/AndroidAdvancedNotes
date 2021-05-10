package com.zero.suspensiondemo;

import android.app.Application;
import android.content.Context;


public class MyApp extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }


    public static Context getAppContext(){
        return application.getApplicationContext();
    }

}
