package com.arch.demo.common.arouter.news;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface INewsService extends IProvider {
    String NEWS_ROUTER = "/news/";
    String NEWS_SERVICE = NEWS_ROUTER + "news_service";
    Fragment getHeadlineNewsFragment();
}
