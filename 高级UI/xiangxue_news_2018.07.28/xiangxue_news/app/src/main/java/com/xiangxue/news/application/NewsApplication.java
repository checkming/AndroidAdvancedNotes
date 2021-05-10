package com.xiangxue.news.application;

import com.billy.cc.core.component.CC;
import com.kingja.loadsir.core.LoadSir;
import com.xiangxue.base.BaseApplication;
import com.xiangxue.base.loadsir.CustomCallback;
import com.xiangxue.base.loadsir.EmptyCallback;
import com.xiangxue.base.loadsir.ErrorCallback;
import com.xiangxue.base.loadsir.LoadingCallback;
import com.xiangxue.base.loadsir.TimeoutCallback;
import com.xiangxue.network.ApiBase;
import com.xiangxue.news.BuildConfig;

public class NewsApplication extends BaseApplication {

    @Override
    public void onCreate(){
        super.onCreate();
        setDebug(BuildConfig.DEBUG);
        ApiBase.setNetworkRequestInfo(new NetworkRequestInfo());
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
