package com.zero.rxjava2.rxjavathreaddemo;

import io.reactivex.annotations.NonNull;

public interface Emitter<T> {
    void onNext(@NonNull T value);

    void onError(@NonNull Throwable error);

    void onComplete();
}
