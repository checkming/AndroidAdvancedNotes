package com.xiangxue.arch_demo.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.arch.demo.core.loadsir.CustomCallback;
import com.arch.demo.core.loadsir.EmptyCallback;
import com.arch.demo.core.loadsir.ErrorCallback;
import com.arch.demo.core.loadsir.LoadingCallback;
import com.arch.demo.core.loadsir.TimeoutCallback;
import com.arch.demo.core.preference.PreferencesUtil;
import com.arch.demo.core.utils.ToastUtil;
import com.kingja.loadsir.core.LoadSir;
import com.xiangxue.network.base.NetworkApi;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class ArchDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesUtil.init(this);
        NetworkApi.init(new NetworkRequestInfo(this));
        ToastUtil.init(this);
        ARouter.init(this);
        ARouter.openDebug();
        ARouter.openLog();
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
    }
}
