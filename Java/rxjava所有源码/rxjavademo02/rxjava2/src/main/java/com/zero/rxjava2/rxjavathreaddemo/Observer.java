package com.zero.rxjava2.rxjavathreaddemo;

import io.reactivex.annotations.NonNull;

public interface Observer<T> {


    void onSubscribe(@NonNull Disposable d);


    void onNext(@NonNull T t);


    void onError(@NonNull Throwable e);

    void onComplete();
}
