package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Trace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static void verifyStoragePermissions(AppCompatActivity activity) {


        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RecyclerView mRecycler;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        verifyStoragePermissions(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        mRecycler = findViewById(R.id.rv_list);


        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        //虚拟数据
        mData = createDataList();

        //设置Adapter必须指定，否则数据怎么显示
        mRecycler.setAdapter(new RecyclerViewDemo1Adapter(mData));

        //设置分割线
        mRecycler.addItemDecoration(
                new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        Trace.endSection();
    }


    protected List<String> createDataList() {
        mData = new ArrayList<>();
        for (int i=0;i<20;i++){
            mData.add("这是第"+i+"个View");
        }
        return mData;
    }
}
