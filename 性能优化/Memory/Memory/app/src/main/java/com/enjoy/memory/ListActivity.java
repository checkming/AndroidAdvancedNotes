package com.enjoy.memory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


public class ListActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView rv = findViewById(R.id.rv);
        rv.setAdapter(new MyAdapter(this));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("a","a");
    }
}
