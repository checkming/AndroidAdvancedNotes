package com.arch.demo.application;

import com.arch.demo.BuildConfig;
import com.arch.demo.network_api.INetworkRequestInfo;

import java.util.HashMap;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NetworkRequestInfo implements INetworkRequestInfo{
    HashMap<String, String> headerMap = new HashMap<>();

    public NetworkRequestInfo(){
        headerMap.put("os", "android");
        headerMap.put("versionName", BuildConfig.VERSION_NAME);
        headerMap.put("versionCode", String.valueOf(BuildConfig.VERSION_CODE));
        headerMap.put("applicationId", BuildConfig.APPLICATION_ID);
    }

    @Override
    public HashMap<String, String> getRequestHeaderMap() {
        return headerMap;
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
