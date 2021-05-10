package com.xiangxue.alvin.viewpagerwrap;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private List<Integer> images;

    private int index;
    private int preIndex;
    private Timer timer = new Timer();
    private boolean isContinue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bannerlayout);

        viewPager = findViewById(R.id.view_Pager);
        radioGroup = findViewById(R.id.radio_group);
        images = new ArrayList<>();
        images.add(R.drawable.p1);
        images.add(R.drawable.p2);
        images.add(R.drawable.p3);
        images.add(R.drawable.girl5);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(images,this);
        viewPager.setPageMargin(30);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setPageTransformer(true, new PageTransform());
        viewPager.setCurrentItem(images.size() * 100);
        initRadioButton(images.size());

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isContinue) {
                    handler.sendEmptyMessage(1);
                }
            }
        }, 2000, 2000);

    }

    private void initRadioButton(int length) {
        for (int i = 0; i < length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.rg_selector);
            imageView.setPadding(20, 0, 0, 0);
            radioGroup.addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.getChildAt(0).setEnabled(false);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            index = position;
            setCurrentDot(index % images.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setCurrentDot(int i) {
        if (radioGroup.getChildAt(i) != null) {
            //当前按钮不可改变
            radioGroup.getChildAt(i).setEnabled(false);
        }
        if (radioGroup.getChildAt(preIndex) != null) {
            //上个按钮可以改变
            radioGroup.getChildAt(preIndex).setEnabled(true);
            //当前位置变为上一个，继续下次轮播
            preIndex = i;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    index++;
                    viewPager.setCurrentItem(index);
            }
        }
    };


}
