package com.arch.demo.core.customview;

import android.view.View;

public interface ICustomViewActionListener {
    String ACTION_ROOT_VIEW_CLICKED = "action_root_view_clicked";

    void onAction(String action, View view, BaseCustomViewModel viewModel);
}
