package com.xiangxue.webview.command;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.xiangxue.webview.ICallbackFromMainToWeb;
import com.xiangxue.webview.IWebToMain;
import com.xiangxue.webview.mainprocess.MainProHandleRemoteService;
import com.xiangxue.webview.utils.MainLooper;
import com.xiangxue.webview.utils.WebConstants;
import com.xiangxue.webview.BaseWebView;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * WebView所有请求分发
 * <p>
 * 规则：
 * <p>
 * 1、先处理UI依赖
 * 2、再判断是否是跨进程通信，跨进程通信需要通过AIDL方式分发数据
 */
public class CommandDispatcher {
    private Context mContext;
    private static CommandDispatcher sInstance;
    // 实现跨进程通信的接口
    private IWebToMain mWebAidlInterface;

    public static CommandDispatcher getsInstance() {
        if (sInstance == null) {
            synchronized (CommandDispatcher.class) {
                if (sInstance == null) {
                    sInstance = new CommandDispatcher();
                }
            }
        }
        return sInstance;
    }

    public void initAidlConnect(final Context context) {
        mContext = context;
        if (mWebAidlInterface != null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectToMainProcessService();
            }
        }).start();
    }

    public void exec(String cmd, String params, final WebView webView) {
        if (mWebAidlInterface != null) {
            try {
                mWebAidlInterface.handleWebAction(cmd, params, new ICallbackFromMainToWeb.Stub() {
                    @Override
                    public void onResult(int responseCode, String actionName, String response) {
                        handleCallback(actionName, response, webView);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCallback(final String actionName, final String response,
                                final WebView webView) {
        Log.d("CommandDispatcher", String.format("Callback result: action= %s, result= %s", actionName, response));
        MainLooper.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Map params = new Gson().fromJson(response, Map.class);
                if (params.get(WebConstants.NATIVE2WEB_CALLBACK) != null && !TextUtils.isEmpty(params.get(WebConstants.NATIVE2WEB_CALLBACK).toString())) {
                    if (webView instanceof BaseWebView) {
                        ((BaseWebView) webView).handleCallback(response);
                    }
                }
            }
        });
    }
    private IWebToMain mBinderPool;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

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

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {   // 5

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IWebToMain.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
                mWebAidlInterface = IWebToMain.Stub.asInterface(mBinderPool.asBinder());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {    // 6
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectToMainProcessService();
        }
    };

}
