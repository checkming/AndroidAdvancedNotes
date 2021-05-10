package com.zero.activityhookdemo.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;


public class ActivityThreadHandlerCallback implements Handler.Callback {

    private static final String TAG = "Zero";

    Handler mBase;

    public ActivityThreadHandlerCallback(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.i(TAG, "handleMessage: " + msg.what);
        switch (msg.what) {
            // ActivityThread里面 "LAUNCH_ACTIVITY" 这个字段的值是100
            // 本来使用反射的方式获取最好, 这里为了简便直接使用硬编码
            // 在H类中，定义了几十种消息，比如说LAUNCH_ACTIVITY的值是100，PAUSE_ACTIVITY的值是101。从100到109，都是给Activity的生命周期函数准备的
            case 100:
                handleLaunchActivity(msg);
                break;
            case 159:   //for API Build.VERSION_CODES.O
                handleActivity(msg);
                break;
        }
        mBase.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        // 这里简单起见,直接取出TargetActivity;

        Object obj = msg.obj;
        Log.i(TAG, "handleLaunchActivity: " + obj);
        // 根据源码:
        // 这个对象是 ActivityClientRecord 类型
        // 我们修改它的intent字段为我们原来保存的即可.
        // switch (msg.what) {
        //      case LAUNCH_ACTIVITY: {
        //          Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
        //          final ActivityClientRecord r = (ActivityClientRecord) msg.obj;

        //          r.packageInfo = getPackageInfoNoCheck(
        //                  r.activityInfo.applicationInfo, r.compatInfo);
        //         handleLaunchActivity(r, null);
        try {
            // 把替身恢复成真身
            Field intent = obj.getClass().getDeclaredField("intent");
            intent.setAccessible(true);
            Intent raw = (Intent) intent.get(obj);
            Log.i(TAG, "handleLaunchActivity: raw " + raw);
            Intent target = raw.getParcelableExtra(HookHelper.EXTRA_TARGET_INTENT);
            Log.i(TAG, "handleLaunchActivity: target " + target);
            raw.setComponent(target.getComponent());

        } catch (Exception e) {
            Log.e(TAG, "handleLaunchActivity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleActivity(Message msg) {
        // 这里简单起见,直接取出TargetActivity;
        Log.i(TAG, "handleActivity: msg " + msg);
        try{
            Object obj = msg.obj;
            Log.i(TAG, "handleActivity: obj " + obj);
            Field mActivityCallbacksField = obj.getClass().getDeclaredField("mActivityCallbacks");
            mActivityCallbacksField.setAccessible(true);
            List<Object> mActivityCallbacks = (List<Object>) mActivityCallbacksField.get(obj);

            if(mActivityCallbacks.size() > 0) {
                String className = "android.app.servertransaction.LaunchActivityItem";
                if(mActivityCallbacks.get(0).getClass().getCanonicalName().equals(className)) {
                    Object object = mActivityCallbacks.get(0);
                    Field intentField = object.getClass().getDeclaredField("mIntent");
                    intentField.setAccessible(true);
                    Intent intent = (Intent) intentField.get(object);
                    Intent target = intent.getParcelableExtra(HookHelper.EXTRA_TARGET_INTENT);
                    intent.setComponent(target.getComponent());
                }
            }
        }catch(Exception e){
            Log.e(TAG, "handleActivity: "+ e.getMessage() );
            e.printStackTrace();
        }


    }
}
