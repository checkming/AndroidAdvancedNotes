package com.zero.rxjava2;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class RxPreLoader<T> {

    private BehaviorSubject<T> mData;

    private Disposable disposable;

    public RxPreLoader(T defaultValue) {
        mData = BehaviorSubject.createDefault(defaultValue);
    }

    public void publish(T obj) {
        mData.onNext(obj);
    }

    public Disposable subscribe(Consumer onNext) {
        disposable = mData.subscribe(onNext);
        return disposable;
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public BehaviorSubject<T> getCache() {
        return mData;
    }

    public T getLastCacheData() {
        return mData.getValue();
    }

}
