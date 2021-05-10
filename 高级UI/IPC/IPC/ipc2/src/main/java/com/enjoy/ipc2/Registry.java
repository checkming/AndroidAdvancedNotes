package com.enjoy.ipc2;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负责记录 服务端注册的信息
 */
public class Registry {
    private static volatile Registry mInstance;

    //服务表
    private ConcurrentHashMap<String, Class<?>> mServices = new ConcurrentHashMap<>();
    //方法表
    private ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mMethods =
            new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Object> objects =
            new ConcurrentHashMap<>();

    public static Registry getInstance() {
        if (mInstance == null) {
            synchronized (IPC2.class) {
                if (mInstance == null) {
                    mInstance = new Registry();
                }
            }
        }
        return mInstance;
    }


    public void register(Class<?> service) {
        //1.服务id 与 class的表
        ServiceId serviceId = service.getAnnotation(ServiceId.class);
        if (null == serviceId) {
            throw new RuntimeException("必须使用ServiceId注解的服务才能注册！");
        }
        String value = serviceId.value();
        mServices.put(value, service);

        //2.class与方法的表
        ConcurrentHashMap<String, Method> methods = mMethods.get(service);
        if (methods == null) {
            methods = new ConcurrentHashMap<String, Method>();
            mMethods.put(service, methods);
        }
        for (Method method : service.getMethods()) {
            //因为有重载方法的存在，所有不能以方法名 作为key！ 带上参数作为key！
            StringBuilder builder = new StringBuilder(method.getName());
            builder.append("(");

            //方法的参数类型数组
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 0) {
                builder.append(parameterTypes[0].getName());
            }


            for (int i = 1; i < parameterTypes.length; i++) {
                builder.append(",").append(parameterTypes[i].getName());
            }

            builder.append(")");

            methods.put(builder.toString(), method);
        }

        Set<Map.Entry<String, Class<?>>> entries = mServices.entrySet();
        for (Map.Entry<String, Class<?>> entry : entries) {
            System.out.println("服务表:" + entry.getKey() + " = " + entry.getValue());
        }

        Set<Map.Entry<Class<?>, ConcurrentHashMap<String, Method>>> entrySet = mMethods.entrySet();
        for (Map.Entry<Class<?>, ConcurrentHashMap<String, Method>> m : entrySet) {
            System.out.println("方法表：" + m.getKey());
            ConcurrentHashMap<String, Method> value1 = m.getValue();
            for (Map.Entry<String, Method> stringMethodEntry : value1.entrySet()) {
                System.out.println(" " + stringMethodEntry.getKey());
            }
        }

    }

    /**
     * 查找对应的Methond
     *
     * @param serviceId
     * @param methodName
     * @param objects
     * @return
     */
    public Method findMethod(String serviceId, String methodName, Object[] objects) {
        Class<?> service = mServices.get(serviceId);
        ConcurrentHashMap<String, Method> methods = mMethods.get(service);

        StringBuilder builder = new StringBuilder(methodName);
        builder.append("(");
        if (objects.length != 0) {
            builder.append(objects[0].getClass().getName());
        }
        for (int i = 1; i < objects.length; i++) {
            builder.append(",").append(objects[0].getClass().getName());
        }
        builder.append(")");

        return methods.get(builder.toString());
    }


    public void putInstanceObject(String serviceId, Object object) {
        objects.put(serviceId, object);
    }

    public Object getInstanceObject(String serviceId) {
        return objects.get(serviceId);
    }
}
