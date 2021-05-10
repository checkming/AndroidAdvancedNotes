package com.zero.publishsubcribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体的被观察者角色
 * 微信公众号服务
 */
public class WechartServer implements Observable {

    /**
     * 观察者的清单
     */
    private List<Observer> list;

    private String message;

    public WechartServer(){
        list = new ArrayList<>();
    }

    @Override
    public void add(Observer observer) {
        list.add(observer);
    }

    @Override
    public void remove(Observer observer) {
        list.remove(observer);
    }

    public void pushMessage(String msg){
        this.message = msg;
        System.out.println("微信服务号更新消息了： " + msg);
        //通知所有关注了本服务号的小伙伴
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (Observer observer:list) {
            observer.update(message);
        }
    }
}
