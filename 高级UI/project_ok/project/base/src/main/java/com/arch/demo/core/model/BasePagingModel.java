package com.arch.demo.core.model;

import java.lang.ref.WeakReference;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public abstract class BasePagingModel<T> extends SuperBaseModel<T> {
    protected boolean isRefresh = true;
    protected int pageNumber = 0;

    /**
     * 发消息给UI线程
     *
     * @param isEmpty     model的整个数据是否为空
     * @param isFirstPage 是否是第一页
     * @param hasNextPage 是否还有下一页
     */
    protected void loadSuccess(T data, final boolean isEmpty, final boolean isFirstPage,
                               final boolean hasNextPage) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener listenerItem = (IModelListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFinish(BasePagingModel.this, data, isEmpty, isFirstPage, hasNextPage);
                        }
                    }
                }
                if (getCachedPreferenceKey() != null && isFirstPage) {
                    saveDataToPreference(data);
                }
            }, 0);
        }
    }

    protected void loadFail(final String prompt, final boolean isFirstPage) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener listenerItem = (IModelListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFail(BasePagingModel.this, prompt, isFirstPage);
                        }
                    }
                }
            }, 0);
        }
    }

    @Override
    protected void notifyCachedData(T data) {
        loadSuccess(data, false, true, true);
    }

    public interface IModelListener<T> extends IBaseModelListener {
        void onLoadFinish(BasePagingModel model, T data, boolean isEmpty, boolean isFirstPage, boolean hasNextPage);
        void onLoadFail(BasePagingModel model, String prompt, boolean isFirstPage);
    }
}
