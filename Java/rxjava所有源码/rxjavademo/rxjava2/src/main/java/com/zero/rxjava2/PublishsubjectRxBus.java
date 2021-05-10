package com.zero.rxjava2;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class PublishsubjectRxBus {

    private final Subject<Object> mBus;

    private PublishsubjectRxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    public static PublishsubjectRxBus get() {
        return Holder.BUS;
    }

    public void post(Object obj) {
        mBus.onNext(obj);
    }

    public <T> Observable<T> tObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable tObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder {
        private static final PublishsubjectRxBus BUS = new PublishsubjectRxBus();
    }

    public static void main(String ... args){
            //TODO:

        PublishsubjectRxBus.get().post("test");

        new Thread(() -> {

//            if(PublishsubjectRxBus.get().hasObservers()){
                PublishsubjectRxBus.get().mBus.subscribe(s ->{
                    System.out.println("s: " + s);
                });
//            }
        }).start();

    }
}
