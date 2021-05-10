package com.arch.demo.common.views.titleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.arch.demo.common.R;
import com.arch.demo.common.databinding.PictureTitleViewBinding;
import com.arch.demo.common.databinding.TitleViewBinding;
import com.arch.demo.common.webview.WebActivity;
import com.arch.demo.core.customview.BaseCustomView;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class TitleView extends BaseCustomView<TitleViewBinding, TitleViewViewModel> {
    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.title_view;
    }

    @Override
    public void setDataToView(TitleViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
        WebActivity.startCommonWeb(view.getContext(), "", getViewModel().link);
    }
}
