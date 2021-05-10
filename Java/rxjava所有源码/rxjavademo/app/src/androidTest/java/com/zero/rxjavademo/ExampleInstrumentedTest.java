package com.zero.rxjavademo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

//        assertEquals("com.zero.rxjavademo", appContext.getPackageName());

        new Thread() {
            @Override
            public void run() {
                System.out.println("Thread run() 所在线程为 :" + Thread.currentThread().getName());
                Observable
                        .create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                System.out.println("Observable subscribe() 所在线程为 :" + Thread.currentThread().getName());
                                emitter.onNext("文章1");
                                emitter.onNext("文章2");
                                emitter.onComplete();
                            }
                        })
                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .observeOn(Schedulers.from(new Executor() {
//                            @Override
//                            public void execute(Runnable runnable) {
//                                System.out.println("excute");
//                                runnable.run();
//                            }
//                        }))
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                System.out.println("Observer onSubscribe() 所在线程为 :" + Thread.currentThread().getName());
                            }

                            @Override
                            public void onNext(String s) {
                                System.out.println("Observer onNext() 所在线程为 :" + Thread.currentThread().getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("Observer onError() 所在线程为 :" + Thread.currentThread().getName());
                            }

                            @Override
                            public void onComplete() {
                                System.out.println("Observer onComplete() 所在线程为 :" + Thread.currentThread().getName());
                            }
                        });
            }
        }.start();
    }
}
