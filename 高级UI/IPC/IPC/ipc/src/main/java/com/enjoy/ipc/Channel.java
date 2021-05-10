package com.enjoy.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.enjoy.ipc.annotation.ServiceId;
import com.enjoy.ipc.model.Parameters;
import com.enjoy.ipc.model.Request;
import com.enjoy.ipc.model.Response;
import com.google.gson.Gson;

import java.util.concurrent.ConcurrentHashMap;

public class Channel {

    /**
     * 单例
     */
    private static volatile Channel instance;

    //已经绑定过的
    private ConcurrentHashMap<Class<? extends IPCService>, Boolean> mBinds =
            new ConcurrentHashMap<>();
    //正在绑定的
    private ConcurrentHashMap<Class<? extends IPCService>, Boolean> mBinding =
            new ConcurrentHashMap<>();
    //已经绑定的服务对应的ServiceConnect
    private final ConcurrentHashMap<Class<? extends IPCService>, IPCServiceConnection> mServiceConnections =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends IPCService>, IIPCService> mBinders =
            new ConcurrentHashMap<>();

    private Channel() {
    }

    public static Channel getInstance() {
        if (null == instance) {
            synchronized (Channel.class) {
                if (null == instance) {
                    instance = new Channel();
                }
            }
        }
        return instance;
    }


    private Gson gson = new Gson();


    public void bind(Context context, String packageName, Class<? extends IPCService> service) {
        IPCServiceConnection ipcServiceConnection;
        //是否已经绑定
        Boolean isBound = mBinds.get(service);
        if (isBound != null && isBound) {
            return;
        }
        //是否正在绑定
        Boolean isBinding = mBinding.get(service);
        if (isBinding != null && isBinding) {
            return;
        }
        //要绑定了
        mBinding.put(service, true);
        ipcServiceConnection = new IPCServiceConnection(service);
        mServiceConnections.put(service, ipcServiceConnection);

        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        }
        context.bindService(intent, ipcServiceConnection, Context.BIND_AUTO_CREATE);
    }


    public void unbind(Context context,  Class<? extends IPCService> service) {
        Boolean bound = mBinds.get(service);
        if (bound != null && bound) {
            IPCServiceConnection connection = mServiceConnections.get(service);
            if (connection != null) {
                context.unbindService(connection);
            }
            mBinds.put(service, false);
        }
    }


    private class IPCServiceConnection implements ServiceConnection {


        private final Class<? extends IPCService> mService;

        public IPCServiceConnection(Class<? extends IPCService> service) {
            mService = service;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IIPCService ipcService = IIPCService.Stub.asInterface(iBinder);
            mBinders.put(mService, ipcService);
            mBinds.put(mService, true);
            mBinding.remove(mService);


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinders.remove(mService);
            mBinds.remove(mService);
        }
    }


    public Response send(int type,Class<? extends IPCService> service, Class<?> classType,
                         String methodName, Object[] parameters) {
        // ipcService: 绑定的服务中onbind返回的binder对象
        IIPCService iipcService = mBinders.get(service);
        if (iipcService == null) {
            //没有绑定服务
            return new Response(null, false);
        }
        //发送请求给服务器
        ServiceId annotation = classType.getAnnotation(ServiceId.class);
        String serviceId = annotation.value();
        Request request = new Request(type, serviceId, methodName, makeParameters(parameters));
        try {
            return iipcService.send(request);
        } catch (RemoteException e) {
            return new Response(null, false);
        }
    }

    //将参数制作为Parameters
    private Parameters[] makeParameters(Object[] parameters) {
        Parameters[] p;
        if (null != parameters) {
            p = new Parameters[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object object = parameters[i];
                p[i] = new Parameters(object.getClass().getName(), gson.toJson(object));
            }
        } else {
            p = new Parameters[0];
        }
        return p;
    }


}
