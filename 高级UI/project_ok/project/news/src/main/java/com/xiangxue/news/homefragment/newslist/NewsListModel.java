package com.xiangxue.news.homefragment.newslist;

import com.arch.demo.common.views.picturetitleview.PictureTitleViewViewModel;
import com.arch.demo.common.views.titleview.TitleViewViewModel;
import com.arch.demo.core.customview.BaseCustomViewModel;
import com.arch.demo.core.model.BasePagingModel;
import com.xiangxue.news.homefragment.api.NewsApi;
import com.arch.demo.network_api.observer.BaseObserver;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.arch.demo.network_api.beans.NewsListBean;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListModel<T> extends BasePagingModel<T> {
    private String mChannelId = "";
    private String mChannelName = "";
    private static final String PREF_KEY_NEWS_CHANNEL = "pref_key_news_";

    @Override
    protected String getCachedPreferenceKey() {
        return PREF_KEY_NEWS_CHANNEL + mChannelId;
    }

    protected Type getTClass() {
        return new TypeToken<ArrayList<PictureTitleViewViewModel>>() {
        }.getType();
    }

    public NewsListModel(String channelId, String channelName) {
        mChannelId = channelId;
        mChannelName = channelName;
    }

    @Override
    public void refresh() {
        isRefresh = true;
        load();
    }

    public void loadNexPage() {
        isRefresh = false;
        load();
    }

    @Override
    protected void load() {
        NewsApi.getInstance().getNewsList(new BaseObserver<NewsListBean>(this) {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                e.printStackTrace();
                loadFail(e.message, isRefresh);
            }

            @Override
            public void onNext(NewsListBean newsChannelsBean) {
                // All observer run on main thread, no need to synchronize
                pageNumber = isRefresh ? 2 : pageNumber + 1;
                ArrayList<BaseCustomViewModel> baseViewModels = new ArrayList<>();

                for (NewsListBean.Contentlist source : newsChannelsBean.showapiResBody.pagebean.contentlist) {
                    if (source.imageurls != null && source.imageurls.size() > 1) {
                        PictureTitleViewViewModel viewModel = new PictureTitleViewViewModel();
                        viewModel.avatarUrl = source.imageurls.get(0).url;
                        viewModel.link = source.link;
                        viewModel.title = source.title;
                        baseViewModels.add(viewModel);
                    } else {
                        TitleViewViewModel viewModel = new TitleViewViewModel();
                        viewModel.link = source.link;
                        viewModel.title = source.title;
                        baseViewModels.add(viewModel);
                    }
                }
                loadSuccess((T) baseViewModels, baseViewModels.size() == 0, isRefresh, baseViewModels.size() == 0);
            }
        }, mChannelId, mChannelName, String.valueOf(isRefresh ? 1 : pageNumber));
    }
}
