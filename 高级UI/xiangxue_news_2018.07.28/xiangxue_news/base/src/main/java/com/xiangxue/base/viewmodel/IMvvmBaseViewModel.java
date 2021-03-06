package com.xiangxue.base.viewmodel;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public interface IMvvmBaseViewModel<V> {

    void attachUI(V view);

    V getPageView();

    boolean isUIAttached();

    void detachUI();
}
