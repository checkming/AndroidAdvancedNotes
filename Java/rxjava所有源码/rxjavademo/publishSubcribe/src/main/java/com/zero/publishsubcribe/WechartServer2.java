package com.zero.publishsubcribe;

import java.util.Observable;

/**
 * 具体的被观察者角色
 * 微信公众号服务
 */
public class WechartServer2 extends Observable {

    private String message;

    public void pushMessage(String msg) {
        this.message = msg;
        System.out.println("微信服务号更新消息了： " + msg);
        setChanged();
        //通知所有关注了本服务号的小伙伴
        notifyObservers(message);
    }


}
