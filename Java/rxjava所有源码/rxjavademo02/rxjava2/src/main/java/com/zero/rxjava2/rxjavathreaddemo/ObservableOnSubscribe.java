package com.zero.rxjava2.rxjavathreaddemo;

import io.reactivex.annotations.NonNull;

public interface ObservableOnSubscribe<T> {
    void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception;
}
