package com.xiangxue.alvin.proxymodel;

import android.app.Application;

import com.xiangxue.alvin.proxymodel.proxy.HttpProxy;
import com.xiangxue.alvin.proxymodel.proxy.OkHttpModel;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.proxymodel
 * @fileName MyApplication
 * @date on 2018/10/18
 * @qq 2464061231
 **/
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        HttpProxy.init(VolleyModel.getInstance(getApplicationContext()));
        HttpProxy.init(new OkHttpModel(getApplicationContext()));
    }
}
