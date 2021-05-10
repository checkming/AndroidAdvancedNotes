package com.zero.rxjava2;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Consumer;

public class SingleDemo {

    public static void main(String ... args){
            //TODO:

        //lambda表达式
        Single.create( emitter -> emitter.onSuccess("test"))
                .subscribe(System.out::println);

        Single.create(emitter -> emitter.onSuccess("test1"))
                .subscribe(System.out::println,Throwable::printStackTrace);

        Single.create(emitter -> emitter.onError(new RuntimeException("出错误了")))
                .subscribe(System.out::println,Throwable::printStackTrace);

        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                emitter.onSuccess("test3");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }
}
