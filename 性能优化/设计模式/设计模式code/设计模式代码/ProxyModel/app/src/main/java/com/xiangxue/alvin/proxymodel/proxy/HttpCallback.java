package com.xiangxue.alvin.proxymodel.proxy;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/5/23.
 */

public abstract class HttpCallback<Result> implements ICallBack{

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> cls = HttpHelper.analysisClazzInfo(this);

        Result objResult = (Result) gson.fromJson(result,
                cls);
        onSuccess(objResult);
    }

    public abstract void onSuccess(Result result);



}
