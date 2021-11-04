package com.xiangxue.alvin.proxymodel.proxy;

import com.google.gson.Gson;
import com.xiangxue.alvin.proxymodel.proxy.HttpHelper;

public abstract class BaseType<T> {
    public T getResult(String response) {
        Gson gson = new Gson();
        Class<?> cls = HttpHelper.analysisClazzInfo(this);
        T objResult = (T) gson.fromJson(response,
                cls);
        return objResult;
    }
}
