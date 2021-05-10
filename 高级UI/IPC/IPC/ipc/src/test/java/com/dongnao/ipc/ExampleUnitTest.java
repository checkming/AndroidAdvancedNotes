package com.dongnao.ipc;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    interface IInterface {
        void test();
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        //代理对象
        IInterface iInterface = (IInterface) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{IInterface.class}
                // callback 回调
                , new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws
                            Throwable {
                        System.out.println("需要执行:" + method.getName());
                        return null;
                    }
                });

        iInterface.test();
    }
}