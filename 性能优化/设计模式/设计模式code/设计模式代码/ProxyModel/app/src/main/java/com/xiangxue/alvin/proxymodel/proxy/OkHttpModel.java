package com.xiangxue.alvin.proxymodel.proxy;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.proxymodel.proxy
 * @fileName OkHttpModel
 * @date on 2018/10/24
 * @qq 2464061231
 **/

//必须实现统一的被代理的行为规范
public class OkHttpModel implements IHttp {
    private static final String TAG = "OkHttpModel";

    private OkHttpClient mOkHttpClient;
    private Handler myHandler;

    public OkHttpModel(Context context) {
        mOkHttpClient = new OkHttpClient();
        myHandler = new Handler();
    }

    @Override
    //真正干活的地方，服务提供方法
    public void post(String url, Map<String, Object> params, ICallBack callback) {

    }

    @Override
    //真正干活的地方，服务提供方法
    public void get(String url, Map<String, Object> params, final ICallBack callback) {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Zh")
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: " + result);
                            callback.onSuccess(result);
                        }
                    });
                } else {
                    callback.onFailure(response.message().toString());
                }
            }
        });
    }
}
