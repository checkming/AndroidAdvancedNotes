package com.xiangxue.webview;

import android.os.Bundle;

import com.xiangxue.webview.R;
import com.xiangxue.webview.basefragment.BaseWebviewFragment;
import com.xiangxue.webview.utils.WebConstants;

public class CommonWebFragment extends BaseWebviewFragment {

    public String url;

    public static CommonWebFragment newInstance(String url) {
        CommonWebFragment fragment = new CommonWebFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_common_webview;
    }

    @Override
    public int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }
}
