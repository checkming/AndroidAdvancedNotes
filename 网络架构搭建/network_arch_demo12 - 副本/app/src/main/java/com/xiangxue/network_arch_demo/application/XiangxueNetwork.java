package com.xiangxue.network_arch_demo.application;

import android.app.Application;

import com.xiangxue.network.BuildConfig;
import com.xiangxue.network.base.INetworkRequiredInfo;

public class XiangxueNetwork implements INetworkRequiredInfo {
    private Application mApplication;
    public XiangxueNetwork(Application application){
        this.mApplication = application;
    }
    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return mApplication;
    }
}
