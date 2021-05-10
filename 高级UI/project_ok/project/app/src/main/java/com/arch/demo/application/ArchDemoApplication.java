package com.arch.demo.application;

import com.arch.demo.BuildConfig;
import com.arch.demo.R;
import com.arch.demo.core.BaseApplication;
import com.arch.demo.core.loadsir.CustomCallback;
import com.arch.demo.core.loadsir.EmptyCallback;
import com.arch.demo.core.loadsir.ErrorCallback;
import com.arch.demo.core.loadsir.LoadingCallback;
import com.arch.demo.core.loadsir.TimeoutCallback;
import com.arch.demo.core.preference.PreferencesUtil;
import com.arch.demo.network_api.ApiBase;
import com.billy.cc.core.component.CC;
import com.kingja.loadsir.core.LoadSir;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class ArchDemoApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesUtil.init(this);
        ApiBase.setNetworkRequestInfo(new NetworkRequestInfo());
        setIsDebug(BuildConfig.DEBUG);
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();

        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableVerboseLog(BuildConfig.DEBUG);
        CC.enableRemoteCC(BuildConfig.DEBUG);
    }
}
