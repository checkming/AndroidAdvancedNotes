package com.xiangxue.webviewapp;

import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.arch.demo.core.BaseApplication;
import com.arch.demo.core.loadsir.CustomCallback;
import com.arch.demo.core.loadsir.EmptyCallback;
import com.arch.demo.core.loadsir.ErrorCallback;
import com.arch.demo.core.loadsir.LoadingCallback;
import com.arch.demo.core.loadsir.TimeoutCallback;
import com.arch.demo.core.preference.PreferencesUtil;
import com.kingja.loadsir.core.LoadSir;
import com.xiangxue.webview.BuildConfig;
import com.xiangxue.webviewapp.webviewcommands.ToastCommand;
import com.xiangxue.webview.remoteprocessservice.OptimizationService;
import com.xiangxue.webviewapp.webviewcommands.LoginCommands;
import com.xiangxue.webviewapp.webviewcommands.ArouterCommands;

public class WebViewApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        PreferencesUtil.init(this);
        setIsDebug(BuildConfig.DEBUG);
        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this); // As early as possible, it is recommended to initialize in the Application
        startService(new Intent(this, OptimizationService.class));
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
        LoginCommands.init();
        ArouterCommands.init();
        ToastCommand.init();
    }
}
