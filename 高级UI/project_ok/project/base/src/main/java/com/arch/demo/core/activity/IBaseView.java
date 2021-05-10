package com.arch.demo.core.activity;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public interface IBaseView {
    void showContent();

    void showLoading();

    void onRefreshEmpty();

    void onRefreshFailure(String message);
}
