package com.enjoy.ipc2;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.enjoy.ipc2.model.Parameters;
import com.enjoy.ipc2.model.Request;
import com.enjoy.ipc2.model.Response;
import com.google.gson.Gson;

import java.util.concurrent.ConcurrentHashMap;

public class Channel {
    private static final Channel ourInstance = new Channel();

    private ConcurrentHashMap<Class<? extends IPCService>, IIPCService> binders =
            new ConcurrentHashMap<>();

    static Gson gson = new Gson();

    public static Channel getInstance() {
        return ourInstance;
    }

    private Channel() {
    }


    /**
     * bind 服务 同APP绑定成功
     * 不同APP呢？
     * todo 1、已经绑定的就别bind了
     * 2、正在bind的也别bind了!
     *
     * @param context
     * @param service
     */
    public void bind(Context context, String packageName, Class<? extends IPCService> service) {
        Intent intent;
        if (!TextUtils.isEmpty(packageName)) {
            //跨app的绑定
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        } else {
            intent = new Intent(context, service);
        }
        context.bindService(intent, new IPCServiceConnection(service), Context.BIND_AUTO_CREATE);
    }


    class IPCServiceConnection implements ServiceConnection {

        private final Class<? extends IPCService> mService;

        public IPCServiceConnection(Class<? extends IPCService> service) {
            mService = service;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IIPCService binder = IIPCService.Stub.asInterface(service);
            binders.put(mService, binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binders.remove(mService);
        }
    }

    public Response send(int type, Class<? extends IPCService> service, String serviceId,
                         String methodName, Object[] parameters) {

        Request request = new Request(type, serviceId, methodName, makeParameters(parameters));
        IIPCService binder = binders.get(service);
        try {
            return binder.send(request);
        } catch (RemoteException e) {
            e.printStackTrace();
            //也可以把null变成错误信息
            return new Response(null, false);
        }
    }

    private Parameters[] makeParameters(Object[] objects) {
        Parameters[] parameters;
        if (objects != null) {
            parameters = new Parameters[objects.length];
            for (int i = 0; i < objects.length; i++) {
                parameters[i] = new Parameters(objects[i].getClass().getName(),
                        gson.toJson(objects[i]));
            }
        } else {
            parameters = new Parameters[0];
        }
        return parameters;
    }
}
