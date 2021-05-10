package com.zero.rxjava1;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Main {


    public static void main(String... args) {

//        backpressure01();
        backpressure02();
    }

    public static void backpressure01() {
        //TODO:
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; ; i++) {   //无限循环发事件
                    subscriber.onNext(i);
                }
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("call: " + integer);
            }
        });
    }

    public static void backpressure02(){
      //TODO:
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; ; i++) {   //无限循环发事件
                    subscriber.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
//           .observeOn(Schedulers.newThread())
           .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("call: " + integer);
            }
        });

    }


}


