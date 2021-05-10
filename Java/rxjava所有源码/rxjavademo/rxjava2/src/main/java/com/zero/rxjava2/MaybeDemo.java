package com.zero.rxjava2;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.functions.Consumer;

public class MaybeDemo {

    public static void main(String ... args){
            //TODO:

        Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> emitter) throws Exception {
                emitter.onSuccess("testA");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s: " + s);
            }
        });

        Maybe.create(emitter -> {
            emitter.onComplete();//跑了onComplete，后面的不会再发射
            emitter.onSuccess("test1");
            emitter.onSuccess("test2");
        }).subscribe(System.out::println);
    }
}
