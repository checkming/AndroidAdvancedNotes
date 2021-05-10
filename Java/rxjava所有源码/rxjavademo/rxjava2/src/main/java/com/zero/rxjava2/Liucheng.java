package com.zero.rxjava2;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Liucheng {

    public static void main(String ... args){
            //TODO:
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("test1");
                emitter.onComplete();
            }
        });
        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe: ");
            }

            @Override
            public void onNext(String str) {
                System.out.println("onNext: " + str);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete: ");
            }
        };
        observable.subscribeOn(Schedulers.io());
        observable.subscribe(observer);

    }
}
