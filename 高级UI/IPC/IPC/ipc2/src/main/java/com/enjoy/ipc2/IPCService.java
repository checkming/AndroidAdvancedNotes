package com.enjoy.ipc2;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.RemoteException;

import com.enjoy.ipc2.model.Parameters;
import com.enjoy.ipc2.model.Request;
import com.enjoy.ipc2.model.Response;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class IPCService extends Service {


    static Gson gson = new Gson();

    @Override
    public IBinder onBind(Intent intent) {
        return new IIPCService.Stub() {
            @Override
            public Response send(Request request) throws RemoteException {
                String methodName = request.getMethodName();
                Parameters[] parameters = request.getParameters();
                String serviceId = request.getServiceId();
                Object[] objects = restoreParameters(parameters);

                Method method = Registry.getInstance().findMethod(serviceId, methodName,objects );

                switch (request.getType()) {
                    //执行单例方法 静态方法
                    case Request.GET_INSTANCE:
                        try {
                            //getInstance
                            Object result = method.invoke(null, objects);
                            Registry.getInstance().putInstanceObject(serviceId,result);
                            return new Response(null,true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Response(null,false);
                        }
                    //执行普通方法
                    case Request.GET_METHOD:
                        try {
                            Object instanceObject = Registry.getInstance().getInstanceObject(serviceId);
                            //获得结果
                            Object result = method.invoke(instanceObject, objects);
                            return new Response(gson.toJson(result),true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Response(null,false);
                        }
                }
                return null;
            }
        };
    }


    protected Object[] restoreParameters(Parameters[] parameters) {
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameters parameter = parameters[i];
            //还原
            try {
                objects[i] = gson.fromJson(parameter.getValue(), Class.forName(parameter.getType()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }


    public static class IPCService0 extends IPCService {
    }

    public static class IPCService1 extends IPCService {
    }

    public static class IPCService2 extends IPCService {
    }
}
