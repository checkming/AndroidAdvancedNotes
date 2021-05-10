package com.zero.publishsubcribe;

public class Client {

    public static void main(String ... args){
            //TODO:
        //创建一个微信公众服务号(被观察者)
        Observable server = new WechartServer();
        //创建小伙伴(观察者)
        Observer av = new User("AV");
        Observer lance = new User("Lance");
        Observer king = new User("king");
        //订阅
        server.add(av);
        server.add(lance);
        server.add(king);

        //微信公众服务号 推送了一条消息
        ((WechartServer) server).pushMessage("Android是我们最有钱途的一个行业");
        //lance老师取消了关注
        System.out.println("==========================");
        server.remove(lance);
        ((WechartServer) server).pushMessage("Rxjava是一种优雅的编程方式");

        System.out.println("=========jdk的观察者模式实现=======");
        WechartServer2 server2 = new WechartServer2();
        //创建观察者
        User2 zero = new User2("Zero");
        User2 mark = new User2("Mark");
        //关注
        server2.addObserver(zero);
        server2.addObserver(mark);

        server2.pushMessage("Av老师是最骚的");


    }
}
