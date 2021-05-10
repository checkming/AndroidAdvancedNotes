package com.zero.rxjava2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

public class ConnectableObservableDemo {

    public static void main(String... args) {
        //TODO:

        /**
         * ConnectableObservable 热的
         */
        ConnectableObservable<Long> connectableObservable = (ConnectableObservable) Observable.create(emitter ->
                Observable.interval(10, TimeUnit.MILLISECONDS,
                        Schedulers.computation())
                        .take(Integer.MAX_VALUE).subscribe(emitter::onNext)
        ).observeOn(Schedulers.newThread()).publish();

        /**
         * 关键点
         */
        connectableObservable.connect();

        Consumer<Long> subscriber1 = (aLong) -> {
            System.out.println("consumer1: " + aLong);
        };

        Consumer<Long> subscriber2 = (aLong) -> {
            System.out.println("    consumer2: " + aLong);
        };

        // 利用refCount转成Cold Observable
        Observable<Long> observable = connectableObservable.refCount();

        Disposable disposable1 = observable.subscribe(subscriber1);
        Disposable disposable2 = observable.subscribe(subscriber2);

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disposable1.dispose();
        disposable2.dispose();

        System.out.println("重新开始数据流");

        disposable1 = observable.subscribe(subscriber1);
        disposable2 = observable.subscribe(subscriber2);

        try {
            Thread.sleep(30L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
