package com.xiangxue.webviewapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xiangxue.webviewapp.router.AppRouter;
import com.xiangxue.webviewapp.R;

@Route(path = AppRouter.APP_TESTB_ACTIVITY)
public class TestActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testb);
    }
}
