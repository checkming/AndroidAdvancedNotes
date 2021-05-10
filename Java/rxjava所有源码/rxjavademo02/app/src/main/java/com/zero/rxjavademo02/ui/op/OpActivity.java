package com.zero.rxjavademo02.ui.op;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zero.rxjavademo02.R;

public class OpActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameLayout, OpFragment.newIntance(), OpFragment.class.getName());
        transaction.commit();
    }
}
