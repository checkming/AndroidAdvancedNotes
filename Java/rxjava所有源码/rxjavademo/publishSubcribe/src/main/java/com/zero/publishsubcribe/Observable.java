package com.zero.publishsubcribe;

/**
 * 抽象被观察者角色
 * 声明 添加，删除 通知观察者的方法
 */
public interface Observable {
    void add(Observer observer);
    void remove(Observer observer);
    void notifyObservers();
}
