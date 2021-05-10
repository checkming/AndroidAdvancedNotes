package com.zero.rxjavademo02.rx;

import android.os.Looper;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ViewClickObservable extends Observable<Object> {

    private final View view;

    public static final Object EVENT = new Object();

    public ViewClickObservable(View view) {
        this.view = view;
    }

    @Override
    protected void subscribeActual(Observer<? super Object> observer) {
        //在这里 干我们的逻辑

        MyListener listener = new MyListener(view, observer);
        observer.onSubscribe(listener);
        view.setOnClickListener(listener);
    }

    static final class MyListener implements Disposable, View.OnClickListener {

        private final View view;
        private final Observer<Object> observer;

        private final AtomicBoolean isDisposable = new AtomicBoolean();

        MyListener(View view, Observer<? super Object> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onClick(View v) {//subscribe
            if (!isDisposed()) {
                //在这里把按钮点击事件发射出去
                observer.onNext(EVENT);
            }
        }

        @Override
        public void dispose() {
            if (isDisposable.compareAndSet(false, true)) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    //判断是否是在UI线程
                    view.setOnClickListener(null);
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(() ->
                            view.setOnClickListener(null)
                    );
                }
            }
        }

        @Override
        public boolean isDisposed() {
            return isDisposable.get();
        }
    }
}
