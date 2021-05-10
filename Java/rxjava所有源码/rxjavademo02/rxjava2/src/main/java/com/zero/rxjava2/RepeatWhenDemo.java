package com.zero.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class RepeatWhenDemo {

    private static final String TAG = "RepeatWhenDemo";


    public static void main(String... args) {
        //TODO:
        repeatWhen01();
    }

    public static void repeatWhen01() {
        //TODO:
        Observable.just(1, 2, 4).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                // 将原始 Observable 停止发送事件的标识（Complete（） /  Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable）
                // 以此决定是否重新订阅 & 发送原来的 Observable
                // 此处有2种情况：
                // 1. 若新被观察者（Observable）返回1个Complete事件，则不重新订阅 & 发送原来的 Observable
                // 2. 若新被观察者（Observable）返回1个Next事件，则重新订阅 & 发送原来的 Observable
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object throwable) throws Exception {

                        // 情况1：若新被观察者（Observable）返回1个Complete事件，则不重新订阅 & 发送原来的 Observable
                        return Observable.empty();
                        // 返回Error
//                        return Observable.error(new Throwable("不再重新订阅事件"));

                        // 情况2：若新被观察者（Observable）返回1个Next事件，则重新订阅 & 发送原来的 Observable
//                         return Observable.just(1);
                    }
                });

            }
        })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG, "对Error事件作出响应：" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });
    }
}
