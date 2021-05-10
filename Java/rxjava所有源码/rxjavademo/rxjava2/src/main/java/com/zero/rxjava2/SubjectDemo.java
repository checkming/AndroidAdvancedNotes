package com.zero.rxjava2;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class SubjectDemo {

    public static void main(String... args) {
        //TODO:

//        asyncSubject();
//        behaviorSubject();
//        replaySubject();
        publishSubject();
        publishSubject1();
    }

    public static void asyncSubject() {
        //TODO:
        System.out.println("==============asyncSubject===============");
        AsyncSubject<String> subject = AsyncSubject.create();

        subject.onNext("async1");
        subject.onNext("async2");
//        subject.onComplete();

        subject.subscribe(s -> System.out.println("async: " + s)
                , e -> System.out.println("async error")
                , () ->
                System.out.println("asyncSubject:complete")
        );

        subject.onNext("async3");
        subject.onNext("async4");
        subject.onComplete();
    }

    public static void behaviorSubject() {
        //TODO:
        System.out.println("==============behaviorSubject===============");
        BehaviorSubject<String> subject = BehaviorSubject.createDefault("behaviorSubject1");
        subject.onNext("behaviorSubject11");
        subject.subscribe(s -> System.out.println("behaviorSubject: " + s),
                e -> System.out.println("behaviorSubject error"), () ->
                System.out.println("behaviorSubject:complete")
        );

        subject.onNext("behaviorSubject2");
        subject.onNext("behaviorSubject3");
        subject.onNext("behaviorSubject4");

    }

    public static void replaySubject() {
        //TODO:
        System.out.println("==============replaySubject===============");
//        ReplaySubject<String> subject = ReplaySubject.create();
        ReplaySubject<String> subject = ReplaySubject.createWithSize(2);
        subject.onNext("replaySubject1");
        subject.onNext("replaySubject2");
        subject.onNext("replaySubject21");
        subject.subscribe(s -> System.out.println("replaySubject: " + s)
                , e -> System.out.println("replaySubject error")
                , () -> System.out.println("replaySubject:complete")
        );

        subject.onNext("replaySubject3");
        subject.onNext("replaySubject4");

    }


    public static void publishSubject() {
        //TODO:
        System.out.println("==============publishSubject===============");
        PublishSubject<String> subject = PublishSubject.create();
        subject.onNext("publishSubject1");
        subject.onNext("publishSubject2");
//        subject.onComplete();
        subject.subscribe(s -> System.out.println("publishSubject: " + s)
                , e -> System.out.println("publishSubject error")
                , () -> System.out.println("publishSubject:complete")
        );

        subject.onNext("publishSubject3");
        subject.onNext("publishSubject4");
        subject.onComplete();
    }

    public static void publishSubject1() {
        //TODO:
        System.out.println("==============publishSubject1===============");
        PublishSubject<String> subject = PublishSubject.create();
        subject.subscribeOn(Schedulers.io())
                .subscribe(s -> System.out.println("publishSubject: " + s)
                , e -> System.out.println("publishSubject error")
                , () -> System.out.println("publishSubject:complete")
        );

        subject.onNext("foo");
        subject.onNext("bar");
        subject.onComplete();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
