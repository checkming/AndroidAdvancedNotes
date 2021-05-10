package com.arch.demo.core.baseservices;

public interface IAppInfoService {
    String APP_INFO_SERVICE_NAME = "app_info_service";
    String getApplicationName();
    String getApplciationVersionName();
    String getApplicationVersionCode();
    boolean getApplicationDebug();

}
