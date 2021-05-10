package com.zero.rxjavademo02.ui.retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zero.rxjavademo02.R;
import com.zero.rxjavademo02.bean.ProjectBean;
import com.zero.rxjavademo02.bean.ProjectItem;
import com.zero.rxjavademo02.rx.MyRxView;
import com.zero.rxjavademo02.rx.RxUtils;
import com.zero.rxjavademo02.rx.WanAndroidApi;
import com.zero.rxjavademo02.util.HttpUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RetrofitTestActivity extends RxAppCompatActivity {
    private static final String TAG = "RetrofitTestActivity";

    Button btn1;
    Button btn2;
    Button btn3;

    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        //获取网络api
        WanAndroidApi wanAndroidApi = HttpUtil.getOnlineCookieRetrofit()
                .create(WanAndroidApi.class);


        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);


        btn1.setOnClickListener(v ->
                wanAndroidApi.getProject()
                        .compose(RxUtils.io_main())
                        .subscribe(projectBean -> Log.i(TAG, "projectBean: " + projectBean))
        );

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disposable = wanAndroidApi.getProjectItem(1,294)
                        .compose(RxUtils.io_main())
                        .subscribe(new Consumer<ProjectItem>() {
                            @Override
                            public void accept(ProjectItem projectItem) throws Exception {
                                Log.i(TAG, "accept: " + projectItem);
                            }
                        });
            }
        });

        //网络请求嵌套
        //功能防抖
        //负面教程
//        RxView.clicks(btn3)
//                .throttleFirst(1000, TimeUnit.MILLISECONDS)
//                .subscribe(
//                        v -> wanAndroidApi.getProject()
//                        .compose(RxUtils.io_main())
//                        .subscribe(projectBean -> {
//                            for(ProjectBean.DataBean dataBean :projectBean.getData()){
//                                wanAndroidApi.getProjectItem(1,dataBean.getId())
//                                        .compose(RxUtils.io_main())
//                                        .subscribe( projectItem -> Log.i(TAG, "projectItem: " + projectItem));
//                            }
//                        })
//                );
        // flatMap
        MyRxView.clicks(btn3)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .flatMap(new Function<Object, ObservableSource<ProjectBean>>() {
                    @Override
                    public ObservableSource<ProjectBean> apply(Object o) throws Exception {
                        return wanAndroidApi.getProject();
                    }
                })
                .flatMap(new Function<ProjectBean, ObservableSource<ProjectBean.DataBean>>() {
                    @Override
                    public ObservableSource<ProjectBean.DataBean> apply(ProjectBean projectBean) throws Exception {
                        return Observable.fromIterable(projectBean.getData());
                    }
                })
                .flatMap(new Function<ProjectBean.DataBean, ObservableSource<ProjectItem>>() {
                    @Override
                    public ObservableSource<ProjectItem> apply(ProjectBean.DataBean dataBean) throws Exception {
                        return wanAndroidApi.getProjectItem(1,dataBean.getId());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProjectItem>() {
                    @Override
                    public void accept(ProjectItem projectItem) throws Exception {
                        Log.i(TAG, "accept: " + projectItem);
                    }
                });

        //lambda方式
//        RxView.clicks(btn3)
//                .throttleFirst(1000, TimeUnit.MILLISECONDS)
//                .observeOn(Schedulers.io())
//                .flatMap( o -> wanAndroidApi.getProject())
//                .flatMap(projectBean -> Observable.fromIterable(projectBean.getData()))
//                .flatMap(dataBean -> wanAndroidApi.getProjectItem(1,dataBean.getId()))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(projectItem -> Log.i(TAG, "projectItem: " + projectItem));

        RxView.clicks(btn3)
                .throttleFirst(1000,TimeUnit.MICROSECONDS)
                .observeOn(Schedulers.io())
                .concatMap( o ->wanAndroidApi.getProject())
                .concatMap(projectBean -> Observable.fromIterable(projectBean.getData()))
                .filter( dataBean -> dataBean.getId() > 1)
                .concatMap(dataBean -> wanAndroidApi.getProjectItem(1,dataBean.getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(projectItem -> Log.i(TAG, "projectItem: " + projectItem));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
