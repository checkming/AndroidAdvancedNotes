package com.zero.rxjavademo02.ui.rxbinding;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class RxUtils {

    /**
     * 功能防抖
     *
     * @param seconds
     * @param action
     * @param target
     */
    public static void setOnClickListeners(int seconds, final Action1<Object> action, View target) {
        RxView.clicks(target)
                .throttleFirst(seconds, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object view) throws Exception {
                        action.onClick(view);
                    }
                });
    }

}
