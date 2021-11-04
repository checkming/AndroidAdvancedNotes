package com.xiangxue.alvin.facademodel;

import com.google.gson.Gson;

public abstract class BaseType<T> {
    public T getResult(String response) {
        Gson gson = new Gson();
        Class<?> cls = HttpHelper.analysisClazzInfo(this);
        T objResult = (T) gson.fromJson(response,
                cls);
        return objResult;
    }
}
