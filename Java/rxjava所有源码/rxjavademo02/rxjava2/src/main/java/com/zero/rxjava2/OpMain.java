package com.zero.rxjava2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OpMain {

    public static void main(String ... args){
            //TODO:

        Observable.interval(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("accept:" + aLong);
                    }
                });
        Observable.interval(1,TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println(5+5+"onSubscribe:");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("onNext:" + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    static class RxThread extends Thread{
        
    }
}
