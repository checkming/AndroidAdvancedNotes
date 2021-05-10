package com.xiangxue.news.homefragment.headlinenews;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import com.arch.demo.core.fragment.MvvmFragment;
import com.google.android.material.tabs.TabLayout;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HeadlineNewsFragment extends MvvmFragment<FragmentHomeBinding, HeadlineNewsViewModel> implements HeadlineNewsViewModel.IMainView{
    HeadlineNewsFragmentAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.refresh();
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initChannels();
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public HeadlineNewsViewModel getViewModel() {
        return new HeadlineNewsViewModel();
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected String getFragmentTag() {
        return "HeadlineNewsFragment";
    }

    public void initChannels() {
        mAdapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        viewDataBinding.viewpager.setAdapter(mAdapter);
        viewDataBinding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(viewDataBinding.tablayout));
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
        //绑定tab点击事件
        viewDataBinding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewDataBinding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onChannelsLoaded(ArrayList<ChannelsModel.Channel> channels) {
        mAdapter.setChannels(channels);
        viewDataBinding.tablayout.removeAllTabs();
        for (ChannelsModel.Channel channel : channels) {
            viewDataBinding.tablayout.addTab(viewDataBinding.tablayout.newTab().setText(channel.channelName));
        }
        viewDataBinding.tablayout.scrollTo(0,0);
    }
}
