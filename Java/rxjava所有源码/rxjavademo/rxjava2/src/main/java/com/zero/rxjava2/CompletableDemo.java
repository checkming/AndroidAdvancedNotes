package com.zero.rxjava2;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class CompletableDemo {

    public static void main(String ... args){
            //TODO:

        Completable.fromAction(() -> {
            System.out.println("hello world!");
        }).subscribe();

        Completable.create( emitter -> {
            try{
                TimeUnit.SECONDS.sleep(1);
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        }).andThen(Observable.range(1,10))
                .subscribe(System.out::println);
    }
}
