package com.zero.flowlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Zero";

    private LinearLayout root;

    private Button mBtnScollTo;
    private Button mBtnScrooBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);

//        findViewById(R.id.btn_scollto).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "移动前scrollTo: scrollx= " + root.getScrollX() + " scrollY= " + root.getScrollY() );
//                root.scrollTo(-50,-50);
//                Log.i(TAG, "移动后scrollTo: scrollx= " + root.getScrollX() + " scrollY= " + root.getScrollY() );
//            }
//        });
//
//        findViewById(R.id.btn_scollby).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "移动后scrollBy: scrollx= " + root.getScrollX() + " scrollY= " + root.getScrollY() );
//                root.scrollBy(-50,-50);
//                Log.i(TAG, "移动后scrollBy: scrollx= " + root.getScrollX() + " scrollY= " + root.getScrollY() );
//            }
//        });


    }
}
