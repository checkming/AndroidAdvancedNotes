package com.enjoy.srl_vp;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

public class SRL_VP_main extends AppCompatActivity {

    private CustomSRL2 swipeRefreshLayout;
    private CustomVPInner viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srl_vp);
        initView();
        initData();
    }

    private void initView() {
        swipeRefreshLayout = (CustomSRL2) findViewById(R.id.swipeRefreshLayout);
        viewPager = (CustomVPInner) findViewById(R.id.viewPager);
    }

    private void initData() {
        viewPager.setAdapter(new SubVpAdapter(getSupportFragmentManager(), DataModel.getFragmentList1()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
