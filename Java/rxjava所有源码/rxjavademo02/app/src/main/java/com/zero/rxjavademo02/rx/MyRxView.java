package com.zero.rxjavademo02.rx;

import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyRxView {
    private static final String TAG = "MyRxView";

    //把按钮点击事件 转化成 Observable<>形式
    public static Observable<Object> clicks(View view){
        return new ViewClickObservable(view);
    }

    public void test(){
        //1 创建一个被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //subscribe利用我们ObservableEmitter(发射器)发射事件
                e.onNext("av");
                e.onNext("lance");
                //按钮点击事件 Onclick
                e.onComplete();
            }
        });
         // 2 .创建一个观察者
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "onNext: 处理事件");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        //3 订阅
        observable.subscribe(observer);
    }
}
