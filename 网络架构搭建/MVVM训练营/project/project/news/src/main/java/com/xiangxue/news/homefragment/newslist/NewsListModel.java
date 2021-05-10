package com.xiangxue.news.homefragment.newslist;

import com.arch.demo.common.views.picturetitleview.PictureTitleViewViewModel;
import com.arch.demo.common.views.titleview.TitleViewViewModel;
import com.arch.demo.core.customview.BaseCustomViewModel;
import com.arch.demo.core.model.MvvmBaseModel;
import com.arch.demo.core.model.PagingResult;
import com.google.gson.reflect.TypeToken;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsListBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListModel extends MvvmBaseModel<ArrayList<BaseCustomViewModel>> {
    private String mChannelId = "";
    private String mChannelName = "";

    protected Type getTClass() {
        return new TypeToken<ArrayList<PictureTitleViewViewModel>>() {
        }.getType();
    }

    public NewsListModel(String channelId, String channelName) {
        super(true, "pref_key_news_" + channelId, null);
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
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId, mChannelName, String.valueOf(isRefresh ? 1 : pageNumber))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>(this) {
                    @Override
                    public void onSuccess(NewsListBean newsChannelsBean) {
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
                        loadSuccess(baseViewModels, new PagingResult(baseViewModels.size() == 0, isRefresh, baseViewModels.size() == 0));
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                        loadFail(e.getMessage(), new PagingResult(true, isRefresh, false));

                    }
                }));
    }
}
