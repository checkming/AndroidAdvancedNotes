package com.arch.demo.core.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.arch.demo.core.customview.BaseCustomViewModel;
import com.arch.demo.core.customview.ICustomView;


/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    ICustomView view;

    public BaseViewHolder(ICustomView view) {
        super((View) view);
        this.view = view;
    }

    public void bind(@NonNull BaseCustomViewModel item) {
        view.setData(item);
    }
}