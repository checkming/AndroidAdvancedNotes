package com.zero.rxjava1demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


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
                "背压测试，异步线程",
                "背压测试16",
                "背压测试17"
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
                if (subscription1 != null)
                    subscription1.unsubscribe();
                break;
            case 2:
                backpressure02();
                break;
            case 3:
                backpressure03();
                break;
            case 4:
                backpressure04();
                break;
            case 9:

                break;
            default:
                break;
        }
    }

    Subscription subscription1;

    public void backpressure01() {
        //TODO:

        subscription1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; ; i++) {   //无限循环发事件
                    subscriber.onNext(i);
//                    try {
//                        Thread.sleep(100L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i("Zero", "call: " + integer);
//                try {
//                    Thread.sleep(1000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                    }
                });
    }

    public void backpressure02() {
        //TODO:
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; ; i++) {   //无限循环发事件
                    subscriber.onNext(i);
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i("Zero", "call: " + integer);
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void backpressure03() {
        //TODO:
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 16; i++) {   //无限循环发事件
                    subscriber.onNext(i);
//                    try {
//                        Thread.sleep(10L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i("Zero", "call: " + integer);
//                        try {
//                            Thread.sleep(1000L);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                });

    }

    public void backpressure04() {
        //TODO:
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 17; i++) {   //无限循环发事件
                    subscriber.onNext(i);
//                    try {
//                        Thread.sleep(10L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i("Zero", "call: " + integer);
//                        try {
//                            Thread.sleep(1000L);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                });

    }


}
