package com.enjoy.ipc;

import android.content.Context;

import com.enjoy.ipc.model.Request;
import com.enjoy.ipc.model.Response;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Lance
 * @date 2019/1/8
 */
public class IPC {

    //服务端
    //==================================================================
    //注册
    public static void regiest(Class<?> service) {
        Registry.getInstance().regiest(service);
    }


    //客户端
    //==================================================================

    /**
     * 连接本APP其他进程 服务
     *
     * @param context
     * @param service
     */
    public static void connect(Context context, Class<? extends IPCService> service) {
        connect(context, null, service);
    }


    /**
     * 连接其他APK进程服务
     *
     * @param context
     * @param packageName apk包名
     * @param service
     */
    public static void connect(Context context, String packageName,
                               Class<? extends IPCService> service) {
        Channel.getInstance().bind(context.getApplicationContext(), packageName, service);
    }


    public static void disConnect(Context context, Class<? extends IPCService> service) {
        Channel.getInstance().unbind(context.getApplicationContext(), service);
    }


    public static <T> T getInstance(Class<? extends IPCService> service, Class<T> instanceClass,
                                    Object... parameters) {
        /**
         * 服务：Location,
         * 方法名：getInstance
         * 参数：[]
         */
        return getInstanceWithName(service, instanceClass, "getInstance", parameters);
    }


    public static <T> T getInstanceWithName(Class<? extends IPCService> service, Class<T> instanceClass, String methodName, Object...
            parameters) {

        if (!instanceClass.isInterface()) {
            throw new IllegalArgumentException("必须以接口进行通信。");
        }
        //服务器响应
        Response response = Channel.getInstance().send(Request.GET_INSTANCE, service,
                instanceClass, methodName, parameters);
        // response： 成功
        if (response.isSuccess()) {
            //返回一个假的对象 动态代理
            return getProxy(instanceClass, service);
        }
        return null;
    }

    private static <T> T getProxy(Class<T> instanceClass, Class<? extends IPCService> service) {
        ClassLoader classLoader = instanceClass.getClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{instanceClass},
                new IPCInvocationHandler(instanceClass, service));
    }


    static class IPCInvocationHandler implements InvocationHandler {

        private final Class<?> instanceClass;
        private final Class<? extends IPCService> service;
        static Gson gson = new Gson();


        public IPCInvocationHandler(Class<?> instanceClass, Class<? extends IPCService> service) {
            this.instanceClass = instanceClass;
            this.service = service;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            /**
             * 请求服务端执行对应的方法。
             */
            Response response = Channel.getInstance().send(Request.GET_METHOD, service, instanceClass,
                    method.getName(), args);

            if (response.isSuccess()) {
                Class<?> returnType = method.getReturnType();
                //不是返回null
                if (returnType != Void.class && returnType != void.class) {
                    //获取Location的json字符
                    String source = response.getSource();
                    //反序列化 回 Location
                    return gson.fromJson(source, returnType);
                }
            }
            return null;
        }
    }

}
