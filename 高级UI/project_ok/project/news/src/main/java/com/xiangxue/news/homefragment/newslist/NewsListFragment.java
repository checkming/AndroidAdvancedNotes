package com.xiangxue.news.homefragment.newslist;

import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;

import com.arch.demo.core.customview.BaseCustomViewModel;
import com.arch.demo.core.fragment.MvvmFragment;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.NewsFragmentBinding;

import java.util.ArrayList;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListFragment extends MvvmFragment<NewsFragmentBinding, NewsListViewModel> implements NewsListViewModel.INewsView {
    private NewsListRecyclerViewAdapter mAdapter;
    private String mChannelId = "";
    private String mChannelName = "";

    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.news_fragment;
    }

    @Override
    public NewsListViewModel getViewModel() {
        Log.e(this.getClass().getSimpleName(), this + ": createViewModel.");
        return new NewsListViewModel(mChannelId, mChannelName);
    }

    @Override
    protected void initParameters() {
        if (getArguments() != null) {
            mChannelId = getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID);
            mChannelName = getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME);
            mFragmentTag = mChannelName;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentTag = "NewsListFragment";
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NewsListRecyclerViewAdapter();
        viewDataBinding.listview.setAdapter(mAdapter);
        viewDataBinding.refreshLayout.setRefreshHeader(new WaterDropHeader(getContext()));
        viewDataBinding.refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
        viewDataBinding.refreshLayout.setOnRefreshListener(refreshlayout -> {
            viewModel.tryToRefresh();
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            viewModel.tryToLoadNextPage();
        });
        setLoadSir(viewDataBinding.refreshLayout);
        showLoading();
    }

    @Override
    public void onNewsLoaded(ArrayList<BaseCustomViewModel> newsItems) {
        if (newsItems != null && newsItems.size() > 0) {
            viewDataBinding.refreshLayout.finishLoadMore();
            viewDataBinding.refreshLayout.finishRefresh();
            showContent();
            mAdapter.setData(newsItems);
        } else {
            onRefreshEmpty();
        }
    }

    /***
     * 重试按钮点击
     */
    protected void onRetryBtnClick() {
        viewModel.tryToRefresh();
    }

    @Override
    protected String getFragmentTag() {
        return mChannelName;
    }
}
