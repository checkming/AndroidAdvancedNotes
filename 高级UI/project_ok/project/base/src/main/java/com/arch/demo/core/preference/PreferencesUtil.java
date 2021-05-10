package com.arch.demo.core.preference;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class PreferencesUtil extends BasePreferences {

    private static PreferencesUtil sInstance;

    public static PreferencesUtil getInstance() {
        if (sInstance == null) {
            synchronized (PreferencesUtil.class) {
                if (sInstance == null) {
                    sInstance = new PreferencesUtil();
                }
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        sApplication = application;
    }


    @Override
    public String getSPFileName() {
        return "common_data";
    }

    private static final String CONFIG = "config";
    private static final String APP_CONFIG = "app_config";
    private static SharedPreferences sp;

    private static SharedPreferences appConfigSp;

    /**
     * 写入boolean变量至sp中
     *
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public void putCodeString(String key, String value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = sApplication.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public String getCodeString(String key, String defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = sApplication.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public void putAppStatusBoolean(String key, boolean value) {
        if (appConfigSp == null) {
            appConfigSp = sApplication.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        }
        appConfigSp.edit().putBoolean(key, value).commit();
    }

    public boolean getAppStatusBoolean(String key, boolean defValue) {
        //(存储节点文件名称,读写方式)
        if (appConfigSp == null) {
            appConfigSp = sApplication.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        }
        return appConfigSp.getBoolean(key, defValue);
    }
}
