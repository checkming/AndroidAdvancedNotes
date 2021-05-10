package com.zero.reflectdemo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Client2 {
    /**
     * 动态代理演示
     *
     * @param args
     */
    public static void main(String... args) {
        //TODO:
        //创建Zero
        IShop zero = new Zero();
        //创建动态代理
        IShop proxyShop = (IShop) Proxy.newProxyInstance(zero.getClass().getClassLoader()
                , zero.getClass().getInterfaces()
                , new InvocationHandler() {

                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        if ("buy".equals(method.getName())) {
                            System.out.println("动态代理");
                            Object result = method.invoke(zero, objects);
                            return result;
                        }
                        return null;
                    }
                });

        proxyShop.buy();
    }
}
