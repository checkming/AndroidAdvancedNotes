package com.xiangxue.alvin.proxymodel.proxy;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.proxymodel.proxy
 * @fileName VolleyModel
 * @date on 2018/10/18
 * @qq 2464061231
 **/

//必须实现统一的被代理的行为规范
public class VolleyModel implements IHttp {

    private static final String TAG = "VolleyModel";
    private static RequestQueue mQueue;
    private static VolleyModel _instance;

    public static VolleyModel getInstance(Context context) {
        if (_instance == null) {
            synchronized (VolleyModel.class) {
                if (_instance == null) {
                    _instance = new VolleyModel(context);
                }
            }
        }
        return _instance;
    }

    private VolleyModel(Context context) {
        mQueue = com.android.volley.toolbox.Volley.newRequestQueue(context);
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallBack callback) {

    }

    @Override
    //真正干活的地方，服务提供方法
    public void get(String url, Map<String, Object> params,final ICallBack callback) {
        String finalUrl = HttpUtils.appendParams(url, params);
        System.out.println(finalUrl);
        StringRequest request = new StringRequest(Request.Method.GET,
                finalUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        System.out.println(response);
                        Log.d(TAG, "onResponse: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
        mQueue.add(request);
    }

    private static class HttpUtils {
        static String appendParams(String url,Map<String, Object> params) {
            StringBuffer sb = new StringBuffer();
            sb.append(url);
            for (Map.Entry<String, Object> set: params.entrySet()) {
                sb.append(set.getKey()).append(":").append(set.getValue());
            }
            return sb.toString();
        }
    }
}
