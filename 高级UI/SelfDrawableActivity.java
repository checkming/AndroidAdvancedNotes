package com.zero.matrixdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SelfDrawableActivity extends Activity {

    private ImageView mImageView;
    private TaskClearDrawable mTaskClearDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_drawable);

        mImageView = findViewById(R.id.imageView);
        mTaskClearDrawable = new TaskClearDrawable(this, Utils.dp2px(400), Utils.dp2px(400));
        mImageView.setImageDrawable(mTaskClearDrawable);

        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i("Zero", "mTaskClearDrawable = " + mTaskClearDrawable.isRunning() );
                if(false == mTaskClearDrawable.isRunning()){
//                    mTaskClearDrawable.start();
                }
            }
        });
        
    }
}
