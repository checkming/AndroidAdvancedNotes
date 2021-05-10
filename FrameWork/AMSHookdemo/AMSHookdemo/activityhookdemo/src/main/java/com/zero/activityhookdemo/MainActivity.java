package com.zero.activityhookdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zero.activityhookdemo.hook.HookHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_show)
    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
//        HookHelper.hookAMSInterceptStartActivity();
//        HookHelper.hookH();
    }

    @OnClick(R.id.btn_hook1)
    public void onBtnHook1Clicked() {
        HookHelper.hookInstrumentation(this);
        Intent intent = new Intent(this, StubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //通过getApplicationContext启动不了
//        getApplicationContext().startActivity(intent);
    }

    @OnClick(R.id.btn_hook2)
    public void onBtnHook2Clicked() {
        HookHelper.hookActivityThreadInstrumentation();
        Intent intent = new Intent(this, StubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getApplicationContext().startActivity(intent);
    }

    @OnClick(R.id.btn_hook3)
    public void onBtnHook3Clicked() {
        HookHelper.hookAMS();
        Intent intent = new Intent(this, StubActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_hook4)
    public void onBtnHook4Clicked() {
        HookHelper.hookAMSInterceptStartActivity();
        HookHelper.hookH();
        Intent intent = new Intent(this, TargetActivity.class);
        startActivity(intent);
    }
}
