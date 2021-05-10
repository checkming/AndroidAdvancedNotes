package com.zero.rxjava2;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * 合并数据源 & 统一展示
 */

public class MergeDemo {

    private static final String TAG = "MergeDemo";
    // 用于存放最终展示的数据
    private static String result = "数据源来自 = ";


    public static void main(String... args) {

        /*
         * 设置第1个Observable：通过网络获取数据
         * 此处仅作网络请求的模拟
         **/
        Observable<String> network = Observable.just("网络");

        /*
         * 设置第2个Observable：通过本地文件获取数据
         * 此处仅作本地文件请求的模拟
         **/
        Observable<String> file = Observable.just("本地文件");

        Observable.zip(network,file, (s1,s2) -> s1 +s2)
                .subscribe(onNext -> Log.d(TAG,"结果: "+onNext));

        /*
         * 通过merge（）合并事件 & 同时发送事件
         **/
        Observable.merge(network, file)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, "数据源有： " + value);
                        result += value + "+";
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    // 接收合并事件后，统一展示
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "获取数据完成");
                        Log.d(TAG, result);
                    }
                });


    }


}