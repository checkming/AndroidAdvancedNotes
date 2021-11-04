package com.xiangxue.alvin.proxymodel.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.proxymodel.proxy
 * @fileName HttpProxy
 * @date on 2018/10/18
 * @qq 2464061231
 **/
//代理类，它包含真实对象,访问者对代理对象的访问，可以被代理对象转变为对真实对象的访问
public class HttpProxy implements IHttp {

    private static IHttp mHttp = null;
    /**
     * 请求Url
     */
    private String mUrl;
    /**
     * 请求参数
     */
    private Map<String, Object> mParams = null;

    private static HttpProxy mInstance;

    private HttpProxy() {
        mParams = new HashMap<>();
        mInstance = this;
    }

    public static HttpProxy obtain() {
        if (mInstance == null) {
            synchronized (HttpProxy.class) {
                if (mInstance == null) {
                    mInstance = new HttpProxy();
                }
            }
        }
        return mInstance;
    }

    public static void init(IHttp http) {
        mHttp = http;
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallBack callback) {
        // 事情的事前准备工作
        mHttp.post(url, params, callback);
        //访问后的处理
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallBack callback) {
        //访问前的预处理
        mHttp.get(url, params, callback);
        //访问后的操作
    }

}
