package com.enjoy.memory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class SecondActivity extends Activity {

    int i = 10;

    static class R1 implements Runnable {
        WeakReference<SecondActivity> activity;

        public R1(SecondActivity activity) {
            this.activity = new WeakReference<SecondActivity>(activity);
        }

        @Override
        public void run() {
            System.out.println(activity.get().i);
        }
    }

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sencond);



        //gc root

        mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println(i);
//            }
//        },1_000);
        mHandler.postDelayed(new R1(this), 10_000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mHandler.removeCallbacksAndMessages(null);






        // 系统BUG：断掉 gc root
        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        try {
            Field mCurRootView = InputMethodManager.class.getDeclaredField("mCurRootView");
            mCurRootView.setAccessible(true);
            mCurRootView.set(im, null);

            Field mNextServedView = InputMethodManager.class.getDeclaredField("mNextServedView");
            mNextServedView.setAccessible(true);
            mNextServedView.set(im, null);

            Field mServedView = InputMethodManager.class.getDeclaredField("mServedView");
            mServedView.setAccessible(true);
            mServedView.set(im, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
