package com.zero.rxjavademo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import io.reactivex.schedulers.Schedulers;


public class MainFragment extends ListFragment {


    public static Fragment newIntance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    ArrayAdapter<String> arrayAdapter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] array = new String[]{
                "背压测试1",
                "背压测试1,取消",
                "Rxjava流程分析",
                "maptest"
        };
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        String item = arrayAdapter.getItem(position);
        Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
        switch (position) {
            case 0:
                backpressure01();
                break;
            case 1:
                if (disposable1 != null)
                    disposable1.dispose();
                break;
            case 2:
                rxjavademo();
                break;
            case 3:
                mapTest();
                break;
            case 4:

                break;
            case 9:

                break;
            default:
                break;
        }
    }

    public void mapTest(){
        Observable.just("test")
                .map(new Function<String,String>(){
                    @Override
                    public String apply(String s) throws Exception {
                        return s + "map";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i("Zero","s: " + s);
                    }
                });
    }

    public void rxjavademo(){
        // 1. 创建一个被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.i("Zero","subscribe: " + Thread.currentThread().getName());
                emitter.onNext("test1");
//                emitter.onComplete();
            }
        });
        //2. 创建一个观察者
        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("Zero","onSubscribe: " + Thread.currentThread().getName());
                Log.i("Zero", "onSubscribe: " +d);
            }

            @Override
            public void onNext(String str) {
                Log.i("Zero","onNext: " + Thread.currentThread().getName());
                Log.i("Zero", "onNext: " + str);
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Zero","onError: " + Thread.currentThread().getName());
                Log.i("Zero", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i("Zero","onComplete: " + Thread.currentThread().getName());
                Log.i("Zero", "onComplete: ");
            }
        };
        observable =observable.map(new Function<String,String>(){

            @Override
            public String apply(String s) throws Exception {
                Log.i("Zero","apply: " + Thread.currentThread().getName());
                return s + "map";
            }
        });
        Observable observableSubscribeOn =observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.single());
        Observable observableObserveOn =observableSubscribeOn.observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.single());
        // 4. 订阅
        observableObserveOn.subscribe(observer);
    }

    Disposable disposable1;

    public void backpressure01() {
        //TODO: RXJava2.0中Observable不再支持背压,既然说Observable不再支持背压，
        // 那么我们随便搞应该就不会报哪个MissingBackpressureException
        disposable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {   //无限循环发事件
                    emitter.onNext(i);
//                    try {
//                        Thread.sleep(10L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                        Log.i("Zero", "call: " + integer);
//                        try {
//                            Thread.sleep(10L);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
    }

    public void backpressure02() {
        //TODO: 解决方案1
        disposable1 = Observable.<Integer>create(emitter -> {
            for (int i = 0; ; i++) {
                emitter.onNext(i);
                Thread.sleep(10L);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i ->
                        Log.i("Zero", "call: " + i)
                );

    }

    public void backpressure03() {
        //TODO: 解决方案1
        disposable1 = Observable.<Integer>create(emitter -> {
            for (int i = 0; ; i++) {
                emitter.onNext(i);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .sample(1, TimeUnit.SECONDS)
                .subscribe(i ->
                        Log.i("Zero", "call: " + i)
                );

    }

        Subscription subscription;
    public void backpressure04() {
        //TODO: lowable.create(FlowableOnSubscribe<T> source, BackpressureStrategy mode)
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d("Zero", "emit 1");
                emitter.onNext(1);
                Log.d("Zero", "emit 2");
                emitter.onNext(2);
                Log.d("Zero", "emit 3");
                emitter.onNext(3);
                Log.d("Zero", "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("Zero", "onNext: " + integer);
                        subscription.request(2);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("Zero", "onComplete");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}
