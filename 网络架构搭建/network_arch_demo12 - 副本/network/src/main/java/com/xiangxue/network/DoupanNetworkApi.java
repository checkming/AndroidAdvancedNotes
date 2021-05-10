package com.xiangxue.network;

import com.xiangxue.network.base.NetworkApi;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;

public class DoupanNetworkApi extends NetworkApi {
    private static volatile DoupanNetworkApi sInstance;

    public static DoupanNetworkApi getInstance() {
        if (sInstance == null) {
            synchronized (DoupanNetworkApi.class) {
                if (sInstance == null) {
                    sInstance = new DoupanNetworkApi();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected Interceptor getInterceptor() {
        return null;
    }

    @Override
    protected <T> Function<T, T> getAppErrorHandler() {
        return null;
    }

    public static  <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    public String getFormal() {
        return "https://api.douban.com";
    }

    @Override
    public String getTest() {
        return "https://api.douban.com";
    }
}
