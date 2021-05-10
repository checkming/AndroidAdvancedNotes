package com.zero.rxjava1demo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: 1.获取fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //TODO: 2.开启一个fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //TODO: 3.向FrameLayout容器添加MainFragment,现在并未真正执行
        transaction.add(R.id.frameLayout, MainFragment.newIntance(), MainFragment.class.getName());
        //TODO: 4.提交事务，真正去执行添加动作
        transaction.commit();

    }


}
