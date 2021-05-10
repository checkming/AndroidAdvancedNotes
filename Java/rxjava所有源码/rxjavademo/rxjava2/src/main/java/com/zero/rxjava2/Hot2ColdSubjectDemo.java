package com.zero.rxjava2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class Hot2ColdSubjectDemo {

    public static void main(String... args) {
        //TODO:

        Consumer<Long> subscriber1 = new Consumer<Long>() {
            @Override
            public void accept(Long o) throws Exception {
                System.out.println("consumer1: " + o);
            }
        };

        Consumer<Long> subscriber2 = new Consumer<Long>() {
            @Override
            public void accept(Long o) throws Exception {
                System.out.println("    consumer2: " + o);
            }
        };

        Consumer<Long> subscriber3 = (o) -> {
            System.out.println("        consumer3: " + o);
        };

        //创建一个"冷"的被观察者
//        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
//            @Override
//            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
//                Observable.interval(10, TimeUnit.MILLISECONDS,
//                        Schedulers.computation())
//                        .take(Integer.MAX_VALUE)
//                        .subscribe(new Consumer<Long>() {
//                            @Override
//                            public void accept(Long aLong) throws Exception {
//                                emitter.onNext(aLong);
//                            }
//                        });
//            }
//        }).observeOn(Schedulers.newThread());

        //使用lambda表达式
        Observable<Long> observable = Observable.create(emitter ->
            Observable.interval(10, TimeUnit.MILLISECONDS,
                    Schedulers.computation())
                    .take(Integer.MAX_VALUE).subscribe(emitter::onNext)
        );
        observable.observeOn(Schedulers.newThread());

        /**
         * 借助Subject 把Cold Observable 转化成 Hot Observable
         */
        PublishSubject<Long> subject = PublishSubject.create();
        observable.subscribe(subject);

        subject.subscribe(subscriber1);
        subject.subscribe(subscriber2);

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        subject.subscribe(subscriber3);

        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
