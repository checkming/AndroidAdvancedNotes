package com.zero.rxjava2;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;

public class RepeatUntilDemo {

    private static final String TAG = "RepeatUntilDemo";

    public static void main(String ... args){
            //TODO:
        repeatUntil();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void repeatUntil(){
      //TODO:
        final long start = System.currentTimeMillis();
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(5)
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        Log.i(TAG,"getAsBoolean: " + System.currentTimeMillis());
                        return System.currentTimeMillis() - start > 5000;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG,"aLong: " + aLong);
                    }
                });
    }
}
