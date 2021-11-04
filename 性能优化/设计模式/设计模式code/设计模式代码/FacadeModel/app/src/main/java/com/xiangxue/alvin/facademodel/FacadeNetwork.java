package com.xiangxue.alvin.facademodel;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Map;

public class FacadeNetwork<T> {
    public interface Callback<T> {
        void onSuccess(T response);
        void onFailed(String failed);
    }

    private static RequestQueue mQueue;
    private static FacadeNetwork _instance;

    private FacadeNetwork(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public static FacadeNetwork getInstance(Context context) {
        if (_instance == null) {
            synchronized (FacadeNetwork.class) {
                if (_instance == null) {
                    _instance = new FacadeNetwork(context);
                }
            }
        }
        return _instance;
    }

    public<T> void get(final String url, final Map<String, Object> params,
                       final Callback<T> callback) {
        String finalUrl = HttpUtils.appendParams(url, params);
        System.out.println(finalUrl);
        StringRequest request = new StringRequest(Request.Method.GET,
                finalUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Class<?> cls = HttpHelper.analysisInterfaceInfo(callback);
                        T objResult = (T)gson.fromJson(response, cls);
                        callback.onSuccess(objResult);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed(error.toString());
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
