package com.zero.rxjava2;


import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class RetryDemo {
    private static final String TAG = "RetryDemo";

    public static void main(String... args) {
        //TODO:
        retry();
    }
    static int count = 0;//重连次数

    public static void retry() {
        //TODO:
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                for (int i = 0; i < 100; i++) {
                    observableEmitter.onNext(i);
                    if (i == 2) {//模拟网络请求出错
                        observableEmitter.onError(new IOException("retry error"));
                    }
                }
                observableEmitter.onComplete();
            }
        }).retry(new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                if (throwable instanceof IOException && count++ < 4) {
                    System.out.println("重连次数： " + count);
                    return true;
                }
                return false;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("integer: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println(throwable.getMessage());
            }
        });

    }

}
