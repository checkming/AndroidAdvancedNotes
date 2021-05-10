package com.zero.publishsubcribe;

/**
 * 抽象观察者
 * 定义一个update方法，当被观察者调用notifyObservers方法时，观察者的
 * update方法会被回调
 */
public interface Observer {

    void update(Object arg0);
}
