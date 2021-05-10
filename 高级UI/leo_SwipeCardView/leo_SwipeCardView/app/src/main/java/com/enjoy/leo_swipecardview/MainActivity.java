package com.enjoy.leo_swipecardview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.enjoy.leo_swipecardview.adapter.UniversalAdapter;
import com.enjoy.leo_swipecardview.adapter.ViewHolder;

import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView rv;
    private UniversalAdapter<SwipeCardBean> adapter;
    private List<SwipeCardBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new SwipeCardLayoutManager());

        mDatas = SwipeCardBean.initDatas();
        adapter = new UniversalAdapter<SwipeCardBean>(MainActivity.this, mDatas, R.layout.item_swipe_card) {
            @Override
            public void convert(ViewHolder ViewHolder, SwipeCardBean swipeCardBean) {
                ViewHolder.setText(R.id.tvName, swipeCardBean.getName());
                ViewHolder.setText(R.id.tvPrecent, swipeCardBean.getPostition() + "/" + mDatas.size());
                Glide.with(MainActivity.this)
                        .load(swipeCardBean.getUrl())
                        .into((ImageView) ViewHolder.getView(R.id.iv));
            }
        };
        rv.setAdapter(adapter);
        CardConfig.initConfig(this);

        Callback callback = new SwipeCardCallback(rv, adapter, mDatas);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);

    }

}
