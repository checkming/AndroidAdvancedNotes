package com.xiangxue.news.homefragment.newslist;

import com.arch.demo.core.customview.BaseCustomViewModel;
import com.arch.demo.core.fragment.IBasePagingView;
import com.arch.demo.core.model.BasePagingModel;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;

import java.util.ArrayList;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListViewModel extends MvvmBaseViewModel<NewsListViewModel.INewsView, NewsListModel> implements BasePagingModel.IModelListener<ArrayList<BaseCustomViewModel>> {
    private ArrayList<BaseCustomViewModel> mNewsList = new ArrayList<>();

    public NewsListViewModel(String classId, String lboClassId) {
        model = new NewsListModel(classId, lboClassId);
        model.register(this);
        model.getCachedDataAndLoad();
    }

    @Override
    public void onLoadFinish(BasePagingModel model, ArrayList<BaseCustomViewModel> data, boolean isEmpty, boolean isFirstPage, boolean hasNextPage) {
        if (getPageView() != null) {
            if (model instanceof NewsListModel) {
                if (isFirstPage) {
                    mNewsList.clear();
                }
                if (isEmpty) {
                    if (isFirstPage) {
                        getPageView().onRefreshEmpty();
                    } else {
                        getPageView().onLoadMoreEmpty();
                    }
                } else {
                    mNewsList.addAll(data);
                    getPageView().onNewsLoaded(mNewsList);
                }
            }
        }
    }

    @Override
    public void onLoadFail(BasePagingModel model, String prompt, boolean isFirstPage) {
        if (getPageView() != null) {
            if (isFirstPage) {
                getPageView().onRefreshFailure(prompt);
            } else {
                getPageView().onLoadMoreFailure(prompt);
            }
        }
    }

    public void tryToRefresh() {
        model.refresh();
    }

    public void tryToLoadNextPage() {
        model.loadNexPage();
    }

    public interface INewsView extends IBasePagingView {
        void onNewsLoaded(ArrayList<BaseCustomViewModel> channels);
    }
}
