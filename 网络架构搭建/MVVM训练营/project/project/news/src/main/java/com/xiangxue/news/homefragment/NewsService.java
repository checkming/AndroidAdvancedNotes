package com.xiangxue.news.homefragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.arch.demo.common.arouter.news.INewsService;
import com.xiangxue.news.homefragment.headlinenews.HeadlineNewsFragment;

@Route(path = INewsService.NEWS_SERVICE)
public class NewsService implements INewsService {
    @Override
    public void init(Context context) {

    }

    @Override
    public Fragment getHeadlineNewsFragment() {
        return new HeadlineNewsFragment();
    }
}
