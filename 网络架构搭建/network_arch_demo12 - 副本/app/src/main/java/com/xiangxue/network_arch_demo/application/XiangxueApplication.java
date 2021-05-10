package com.xiangxue.network_arch_demo.application;

import android.app.Application;

import com.xiangxue.network.base.NetworkApi;

public class XiangxueApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        NetworkApi.init(new XiangxueNetwork(this));
    }
}
