package com.zero.rxjava2.rxjavathreaddemo;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface ObservableEmitter<T> extends Emitter<T> {
    void setDisposable(@Nullable Disposable d);

    void setCancellable(@Nullable Cancellable c);

    boolean isDisposed();

    @NonNull
    ObservableEmitter<T> serialize();

    boolean tryOnError(@NonNull Throwable t);
}
