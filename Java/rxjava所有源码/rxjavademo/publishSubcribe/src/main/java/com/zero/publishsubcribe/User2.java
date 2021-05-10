package com.zero.publishsubcribe;

import java.util.Observable;
import java.util.Observer;

/**
 * 具体的观察者
 */
public class User2 implements Observer {

    private String name;
    private String message;

    public User2(String name){
        this.name = name;
    }


    void read(){
        System.out.println(name +"收到了推送消息: "+ message);
    }

    @Override
    public void update(Observable observable, Object o) {
                this.message = (String)o;
        read();
    }
}
