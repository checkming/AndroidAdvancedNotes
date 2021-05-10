package com.zero.animationdemo01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class InterPolatorAnimation extends Activity{
    private Button mButton = null;
    private Button mButton2 = null;
    private Button mButton3 = null;
    private Button mButton4 = null;
    private Button mButton5 = null;
    private Button mButton6 = null;
    private Button mButton7 = null;
    private Button mButton8 = null;
    private AnimationSet anim = null;
    private TranslateAnimation anim2 = null;
    private ImageView mImageView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interpolatoranim);
		mButton = (Button)findViewById(R.id.button1);
		mImageView = (ImageView)findViewById(R.id.mImageView);
		mImageView.setImageResource(R.drawable.ali);
		//1.通过XML实现
        anim = (AnimationSet) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.interpolatoranim);
        //2.通过代码实现
		anim2 = new TranslateAnimation(0.0f, 500.0f, 0.0f, 0.0f);
        anim2.setDuration(5000);
        
		mButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1.加速插值器模型
				mImageView.startAnimation(anim);
			}
		});
		
		mButton2 = (Button)findViewById(R.id.button2);
		mButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//2.减速插值器模型
				anim2.setInterpolator(new DecelerateInterpolator()); 
				mImageView.startAnimation(anim2);			
			}
		});
		
		mButton3 = (Button)findViewById(R.id.button3);
		mButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//2.加速减速插值器模型
				anim2.setInterpolator(new AccelerateDecelerateInterpolator()); 
				mImageView.startAnimation(anim2);			
			}
		});
		
		mButton4 = (Button)findViewById(R.id.button4);
		mButton4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//2.加速减速插值器模型
				anim2.setInterpolator(new BounceInterpolator()); 
				mImageView.startAnimation(anim2);			
			}
		});
		mButton5 = (Button)findViewById(R.id.button5);
		mButton5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//2.加速减速插值器模型
				anim2.setInterpolator(new CycleInterpolator(0.5f)); 
				mImageView.startAnimation(anim2);			
			}
		});
		
		mButton6 = (Button)findViewById(R.id.button6);
		mButton6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//2.加速减速插值器模型
				anim2.setInterpolator(new CustomInterpolator()); 
				mImageView.startAnimation(anim2);			
			}
		});
		
		mButton7 = (Button)findViewById(R.id.button7);
		mButton7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//2.先反向后加速插值器模型
				anim2.setInterpolator(new AnticipateInterpolator()); 
				mImageView.startAnimation(anim2);			
			}
		});
		
		mButton8 = (Button)findViewById(R.id.button8);
		mButton8.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//2.加速减速插值器模型
				anim2.setInterpolator(new OvershootInterpolator()); 
				mImageView.startAnimation(anim2);			
			}
		});
	}
	
	
	
}
