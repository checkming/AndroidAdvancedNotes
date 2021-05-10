package com.zero.rxjava2;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxjavaDoDemo {

    public static void main(String ... args){
            //TODO:

        Observable.just("hello")
                .doOnNext(s -> System.out.println("doOnNext: " + s))
                .doAfterNext(s -> System.out.println("doAfterNext: " + s))
                .doOnComplete(() -> System.out.println("doOnComplete: "))
                .doOnSubscribe(disposable -> System.out.println("doOnSubscribe: " + disposable))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate: "))
                .doFinally(() -> System.out.println("doFinally: "))
                .doOnEach((notify) -> System.out.println("doOnEach: "
                        + (notify.isOnNext()
                        ? "onNext" :notify.isOnComplete()
                        ? "onComplete":"onEror")))
                .doOnLifecycle(disposable -> System.out.println("doOnLifecycle: " + disposable.isDisposed())
                ,() -> System.out.println("doOnLifecycle run"))
                .subscribe(s -> System.out.println("收到消息: " + s));


//        //1. 创建一个Observable  可被观察的
//        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                if(!emitter.isDisposed()){
//                    emitter.onNext("hello rxjava2");
//                    emitter.onNext("小伙伴们，你们好");
//                }
//                emitter.onComplete();
//            }
//        });
//        //2. 创建一个Observer 观察者
//        Observer<String> observer = new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.i("Zero","onSubscribe: " + d);
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.i("Zero","onNext: " + s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i("Zero","onError: " + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.i("Zero","onComplete ");
//            }
//        };
//        //3 观察者通过订阅(subscribe)被观察者 把它们连接到一起
//        //observer(观察者) 订阅 observable(被观察者)
//        observable.subscribe(observer);
    }
}
