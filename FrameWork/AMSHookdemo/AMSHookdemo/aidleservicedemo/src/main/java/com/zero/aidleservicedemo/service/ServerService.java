package com.zero.aidleservicedemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zero.aidleservicedemo.IMyAidlInterface;

public class ServerService extends Service {
    private static final String TAG = "Zero";
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: " + intent);
        //返回远端的Binder对象的实现类
        //具体实现在远程服务中运行
        return new MyBinder();
    }

    static class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int sendData(String data) throws RemoteException {
            Log.i(TAG, "sendData: "+ data);
            return 10;
        }

        @Override
        public String getData() throws RemoteException {
            Log.i(TAG, "getData: ");
            return "hello my aidl";
        }
    }
}
