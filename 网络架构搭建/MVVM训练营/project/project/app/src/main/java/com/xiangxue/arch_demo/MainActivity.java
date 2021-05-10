package com.xiangxue.arch_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.preference.PreferencesUtil;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.xiangxue.arch_demo.ad.AdFragment;
import com.xiangxue.arch_demo.databinding.ActivityMainBinding;
import com.xiangxue.arch_demo.guide.GuideFragment;
import com.xiangxue.arch_demo.home.MainFragment;
import com.xiangxue.arch_demo.home.MainFragmentPagerAdapter;


public class MainActivity extends MvvmActivity<ActivityMainBinding, MvvmBaseViewModel> implements IMainActivity, ViewPager.OnPageChangeListener {
    private static final String IS_SHOW_GUIDE = "is_show_guide";
    private Fragment mAdFragment = new AdFragment(this);
    private Fragment mGuideFragment = new GuideFragment(this);
    private MainFragment mHomeFragment = new MainFragment();
    private MainFragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        if (PreferencesUtil.getInstance().getBoolean(IS_SHOW_GUIDE, true)) {
            mFragmentPagerAdapter.addFragment(mGuideFragment);
            PreferencesUtil.getInstance().setBoolean(IS_SHOW_GUIDE, false);
        } else {
            mFragmentPagerAdapter.addFragment(mAdFragment);
        }
        mFragmentPagerAdapter.addFragment(mHomeFragment);
        viewDataBinding.viewpageId.setOffscreenPageLimit(1);
        viewDataBinding.viewpageId.setAdapter(mFragmentPagerAdapter);

        // Add page change listener to start some other tasks only if mainFragment is visible such as startLocation, etc.
        viewDataBinding.viewpageId.addOnPageChangeListener(this);
    }

    @Override
    protected void onRetryBtnClick() {
    }

    @Override
    protected MvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (isMainFragmentVisible()) {
            super.onBackPressed();
        }
    }

    @Override
    public void removeMeAndGoNextFragment() {
        mFragmentPagerAdapter.removeIndex0Fragment();
    }

    private boolean isMainFragmentVisible() {
        return mFragmentPagerAdapter.getListSize() == 1;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isMainFragmentVisible()) {// Check if HomeFragment is going to show
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            showSystemUi();
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void showSystemUi() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.show();
        }
    }
}
