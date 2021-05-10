package com.xiangxue.webview.remotewebview.commanddispatcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.xiangxue.webview.IWebToMain;
import com.xiangxue.webview.mainprocess.MainProHandleRemoteService;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 用于remoteweb process 向 main process 获取binder
 */
public class MainProcessConnector {
    private Context mContext;
    private IWebToMain iWebToMain;
    private static volatile MainProcessConnector sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private MainProcessConnector(Context context) {
        mContext = context.getApplicationContext();
        connectToMainProcessService();
    }

    public static MainProcessConnector getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MainProcessConnector.class) {
                if (sInstance == null) {
                    sInstance = new MainProcessConnector(context);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectToMainProcessService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, MainProHandleRemoteService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder getIWebAidlInterface() {
        return iWebToMain.asBinder();
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {   // 5

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWebToMain = IWebToMain.Stub.asInterface(service);
            try {
                iWebToMain.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {    // 6
        @Override
        public void binderDied() {
            iWebToMain.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            iWebToMain = null;
            connectToMainProcessService();
        }
    };
}
