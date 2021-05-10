package com.zero.rxjavademo02.ui.rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.zero.rxjavademo02.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RxJavaSzActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaSzActivity";
    @BindViews({R.id.wlqt, R.id.lx, R.id.hbsjy
            , R.id.hqhc, R.id.lxcx})
    List<TextView> textViews;

    @BindView(R.id.gnfd)
    TextView gnfd;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_sz);
        ButterKnife.bind(this);
    }

    /**
     * 网络嵌套
     *
     * @param v
     */
    public void WANGLUOQIANTAO(View v) {
    }

    /**
     * 轮询
     *
     * @param v
     */
    public void LUNXUN(View v) {

    }

    /**
     * 合并数据源
     *
     * @param v
     */
    public void HEBING(View v) {

    }

    /**
     * 获取缓存
     *
     * @param v
     */
    public void HUOQUCACHE(View v) {

    }

    /**
     * 功能防抖
     *
     * @param v
     */
    @OnClick(R.id.gnfd)
    public void GONGNENGFANGDUO(View v) {
        Log.i(TAG,"GONGNENGFANGDUO: " + v);
        RxUtils.setOnClickListeners(5, new Action1<Object>() {
            @Override
            public void onClick(Object view) {
                Log.e(TAG, "抖了吗？：" + count++);
            }
        }, gnfd);
    }

    /**
     * 联想查询
     *
     * @param v
     */
    public void LIANXIANGCHAXUN(View v) {

    }
}
