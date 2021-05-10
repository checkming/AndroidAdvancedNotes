package com.zero.animationdemo01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class ComplicateViewAnimation extends Activity{
    private ImageView mImageView = null;
    private Button mButton = null;
    private Animation anim = null;
    
    private AlphaAnimation alphaAnimation = null;
    private RotateAnimation rotateAnimation = null;
    private ScaleAnimation scaleAnimation = null;
    private TranslateAnimation translateAnimation = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complicateviewanim);
		mImageView = (ImageView)findViewById(R.id.mImageView);
		mImageView.setImageResource(R.drawable.ali);
		//1. View动画通过XML实现
		anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.complicateanim);
	    anim.setFillAfter(false);
	    
		mButton = (Button)findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mImageView.startAnimation(anim);
			}
		});
		
	}
}
