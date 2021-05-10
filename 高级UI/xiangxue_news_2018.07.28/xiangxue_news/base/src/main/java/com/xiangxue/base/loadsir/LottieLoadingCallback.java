package com.xiangxue.base.loadsir;

import android.content.Context;
import android.view.View;

import com.xiangxue.base.R;
import com.kingja.loadsir.callback.Callback;

/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class LottieLoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_lottie_loading;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return true;
    }
}
