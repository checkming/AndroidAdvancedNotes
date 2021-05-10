package com.xiangxue.alvin.mylazyloadingfragment.lazy3;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.xiangxue.alvin.mylazyloadingfragment.R;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {

    private ViewPager viewPager;  //对应的viewPager
    private ArrayList<Fragment> fragmentsList;//view数组

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vp10);

        viewPager = findViewById(R.id.viewpager01);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        fragmentsList = new ArrayList<>();

        fragmentsList.add(Fragment1.newIntance());
        fragmentsList.add(Fragment2WithViewPager.newIntance());
        fragmentsList.add(Fragment3.newIntance());
        fragmentsList.add(Fragment4.newIntance());
        fragmentsList.add(Fragment5.newIntance());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int itemId = R.id.fragment_1;
                switch (i) {
                    case 0:
                        itemId = R.id.fragment_1;
                        break;
                    case 1:
                        itemId = R.id.fragment_2;
                        break;
                    case 2:
                        itemId = R.id.fragment_3;
                        break;
                    case 3:
                        itemId = R.id.fragment_4;
                        break;
                    case 4:
                        itemId = R.id.fragment_5;
                        break;
                }
                bottomNavigationView.setSelectedItemId(itemId);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        /**
         * 实例化一个PagerAdapter
         * 必须重写的两个方法
         * getCount
         * getItem
         */

        PagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentsList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentsList.size();
            }

        };

        viewPager.setAdapter(pagerAdapter);

    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.fragment_1:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.fragment_2:
                    viewPager.setCurrentItem(1, true);
                    return true;
                case R.id.fragment_3:
                    viewPager.setCurrentItem(2, true);
                    return true;
                case R.id.fragment_4:
                    viewPager.setCurrentItem(3, true);
                    return true;
                case R.id.fragment_5:
                    viewPager.setCurrentItem(4, true);
                    return true;
            }
            return false;
        }

    };
}
