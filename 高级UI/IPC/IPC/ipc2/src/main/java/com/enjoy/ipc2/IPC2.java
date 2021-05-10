package com.enjoy.ipc2;

import android.content.Context;

import com.enjoy.ipc2.model.Request;
import com.enjoy.ipc2.model.Response;

import java.lang.reflect.Proxy;

public class IPC2 {


    //=================================================

    /**
     * 注册接口
     * 服务端需要暴露出去的服务 注册！！！
     */
    public static void register(Class<?> service) {
        Registry.getInstance().register(service);
    }


    //===================================================

    /**
     * 客户端 方法
     */

    public static void connect(Context context, Class<? extends IPCService> service) {
        Channel.getInstance().bind(context, null, service);
    }

    public static void connect(Context context, String packageName,
                               Class<? extends IPCService> service) {
        Channel.getInstance().bind(context, packageName, service);
    }


    public static <T> T getInstance(Class<? extends IPCService> service,
                                    Class<T> classType, Object... parameters) {
        return getInstanceWithName(service,classType,"getInstance",parameters);
    }

    public static <T> T getInstanceWithName(Class<? extends IPCService> service,
                                    Class<T> classType,String methodName, Object... parameters) {
        if (!classType.isInterface()){
            //抛异常！
        }
        ServiceId serviceId = classType.getAnnotation(ServiceId.class);
        Response response = Channel.getInstance().send(Request.GET_INSTANCE, service, serviceId.value(), methodName,
                parameters);
        if (response.isSuccess()){
            return (T) Proxy.newProxyInstance(classType.getClassLoader(),new Class[]{classType},
                    new IPCInvocationHandler(service,serviceId.value()));
        }
        return null;
    }

}
