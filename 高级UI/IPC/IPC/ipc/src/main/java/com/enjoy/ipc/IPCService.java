package com.enjoy.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.enjoy.ipc.model.Parameters;
import com.enjoy.ipc.model.Request;
import com.enjoy.ipc.model.Response;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 与 GPSService在同一个进程： 服务端
 * @author Lance
 * @date 2019/1/8
 */
public abstract class IPCService extends Service {

    static Gson gson = new Gson();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IIPCService.Stub() {

            @Override
            public Response send(Request request) throws RemoteException {
                /**
                 * 执行客户端的请求
                 */
                //serviceid Location
                String serviceId = request.getServiceId();
                //从服务表中获得 对应的Class对象。
                //具体类型  Class<LocationManager>
                Class<?> instanceClass = Registry.getInstance().getService(serviceId);

                Parameters[] parameters = request.getParameters();
                Object[] objects = restoreParameters(parameters);

                //从方法表中获得 对应的Method对象
                String methodName = request.getMethodName();
                Method method = Registry.getInstance().getMethod(instanceClass, methodName,objects);

                Response response;
                //客户端的请求类型
                switch (request.getType()) {
                    //单例方法
                    case Request.GET_INSTANCE:
                        try {
                            Object instance = method.invoke(null, objects);
                            // 单例类的serviceId与 单例对象 保存
                            Registry.getInstance().putObject(serviceId, instance);
                            response = new Response(null, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response = new Response(null, false);
                        }
                        break;
                    //普通方法
                    case Request.GET_METHOD:
                        try {
                            Object object = Registry.getInstance().getObject(serviceId);
                            // getLocation 返回Location
                            Object returnObject = method.invoke(object, objects);
                            response = new Response(gson.toJson(returnObject), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response = new Response(null, false);
                        }
                        break;
                    default:
                        response = new Response(null, false);
                        break;
                }

                return response;
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


    public static class IPCService0 extends IPCService{}
}
