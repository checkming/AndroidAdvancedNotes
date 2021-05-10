package com.arch.demo.core.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.arch.demo.core.model.SuperBaseModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class MvvmBaseViewModel<V, M extends SuperBaseModel> extends ViewModel implements IMvvmBaseViewModel<V> {
    private Reference<V> mUIRef;
    protected M model;

    public void attachUI(V ui) {
        mUIRef = new WeakReference<>(ui);
    }

    @Nullable
    public V getPageView() {
        if (mUIRef == null) {
            return null;
        }
        return mUIRef.get();
    }

    public boolean isUIAttached() {
        return mUIRef != null && mUIRef.get() != null;
    }

    public void detachUI() {
        if (mUIRef != null) {
            mUIRef.clear();
            mUIRef = null;

        }
        if(model != null) {
            model.cancel();
        }
    }
}
