package com.xiangxue.base.viewmodel;

import androidx.lifecycle.ViewModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class MvvmBaseViewModel <V> extends ViewModel implements IMvvmBaseViewModel<V> {
    private Reference<V> mUIRef;

    public void attachUI(V ui) {
        mUIRef = new WeakReference<>(ui);
    }

    @Override
    public V getPageView() {
        return null;
    }

    @Override
    public boolean isUIAttached() {
        return false;
    }

    @Override
    public void detachUI() {

    }


}
