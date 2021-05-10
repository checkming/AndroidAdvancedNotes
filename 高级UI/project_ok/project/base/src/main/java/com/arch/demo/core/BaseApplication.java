package com.arch.demo.core;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class BaseApplication extends Application {

    public static BaseApplication sApplication;
    public static boolean sIsDebug;

    public static void setIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    /**
     * 获取进程名
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }
}
