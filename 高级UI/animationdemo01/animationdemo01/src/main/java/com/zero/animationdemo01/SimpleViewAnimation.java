package com.zero.animationdemo01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class SimpleViewAnimation extends Activity{
    private ImageView mImageView = null;
    private Button mAlphaButton = null;
    private Button mRotateButton = null;
    private Button mScaleButton = null;
    private Button mTranslationButton = null;
    private Animation anim = null;
    private Animation anim2 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simpleviewanim);
		mImageView = (ImageView)findViewById(R.id.mImageView);
		mImageView.setImageResource(R.drawable.ali);
		
		//1. View动画通过XML实现
		anim = AnimationUtils.loadAnimation(getApplicationContext(), 
				                            R.anim.simpleanim);
	    anim.setFillAfter(false);
	    
		//2. View动画通过代码实现    
	    anim2 = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, 
	    		                   0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	    anim2.setDuration(5000);
	    anim2.setFillAfter(false);
	    
		mAlphaButton = (Button)findViewById(R.id.button1);
		mAlphaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
			    anim.setFillAfter(false);
			    mImageView.startAnimation(anim);
			}
		});
		
		mRotateButton = (Button)findViewById(R.id.button2);
		mRotateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
			    anim.setFillAfter(false);
			    mImageView.startAnimation(anim);
			}
		});
		
		mScaleButton = (Button)findViewById(R.id.button3);
		mScaleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
			    anim.setFillAfter(false);
			    mImageView.startAnimation(anim);
			}
		});
		
		mTranslationButton = (Button)findViewById(R.id.button4);
		mTranslationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translation);
			    anim.setFillAfter(false);
			    mImageView.startAnimation(anim);
			}
		});
	}
}
