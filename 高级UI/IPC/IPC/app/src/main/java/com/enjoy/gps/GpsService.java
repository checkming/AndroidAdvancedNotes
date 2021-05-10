package com.enjoy.gps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.enjoy.gps.location.Location;
import com.enjoy.gps.location.LocationManager;
import com.enjoy.ipc2.IPC2;

/**
 * @author Lance
 * @date 2019/1/8
 */
public class GpsService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //定位
        LocationManager.getDefault().setLocation(new Location("岳麓区天之道", 1.1d, 2.2d));

        /**
         * 1、在数据/服务提供方进行服务注册
         */

        IPC2.register(LocationManager.class);
    }
}
