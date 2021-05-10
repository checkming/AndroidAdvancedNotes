package com.zero.activityhookdemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zero.activityhookdemo.hook.HookHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Zero";

    @BindView(R.id.tv_show)
    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.btn_hook1)
    public void onBtnHook1Clicked() {

    }

    @OnClick(R.id.btn_hook2)
    public void onBtnHook2Clicked() {

    }

    @OnClick(R.id.btn_hook3)
    public void onBtnHook3Clicked() {

    }

    @OnClick(R.id.btn_hook4)
    public void onBtnHook4Clicked() {
        Log.i(TAG, "onBtnHook4Clicked: " + Build.VERSION.SDK_INT);
        HookHelper.hookIActivityManager();
        HookHelper.hookHandler();
        Intent intent = new Intent(this,TargetActivity.class);
        startActivity(intent);
    }
}
