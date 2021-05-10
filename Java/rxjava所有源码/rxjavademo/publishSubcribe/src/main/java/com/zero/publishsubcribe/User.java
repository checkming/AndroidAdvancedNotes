package com.zero.publishsubcribe;


/**
 * 具体的观察者
 */
public class User implements Observer {

    private String name;
    private String message;

    public User(String name){
        this.name = name;
    }

    @Override
    public void update(Object arg0) {
        this.message = (String)arg0;
        read();
    }

    void read(){
        System.out.println(name +"收到了推送消息: "+ message);
    }
}
