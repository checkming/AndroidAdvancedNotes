package com.enjoy.ipc2;

import com.enjoy.ipc2.model.Request;
import com.enjoy.ipc2.model.Response;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IPCInvocationHandler implements InvocationHandler {

    private final Class<? extends IPCService> service;
    private final String serviceId;
    static Gson gson = new Gson();

    public IPCInvocationHandler(Class<? extends IPCService> service, String serviceId) {
        this.service = service;
        this.serviceId = serviceId;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * 向服务器发起执行method的请求
         */
        Response response = Channel.getInstance().send(Request.GET_METHOD, service, serviceId, method.getName(), args);
        if (response.isSuccess()){
            //方法返回值
            Class<?> returnType = method.getReturnType();
            if (returnType != void.class && returnType != Void.class) {
                //方法执行后的返回值， json数据
                String source = response.getSource();
                return gson.fromJson(source,returnType);
            }

        }
        return null;
    }
}
