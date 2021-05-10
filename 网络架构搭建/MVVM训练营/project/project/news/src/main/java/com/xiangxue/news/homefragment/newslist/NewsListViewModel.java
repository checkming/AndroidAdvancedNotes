package com.xiangxue.news.homefragment.newslist;

import com.arch.demo.core.customview.BaseCustomViewModel;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListViewModel extends MvvmBaseViewModel<NewsListModel, BaseCustomViewModel> {
    public NewsListViewModel init(String classId, String lboClassId) {
        model = new NewsListModel(classId, lboClassId);
        model.register(this);
        model.getCachedDataAndLoad();
        return this;
    }

    public void tryToLoadNextPage() {
        model.loadNexPage();
    }
}
