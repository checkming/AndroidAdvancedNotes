package com.zero.animationdemo01;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class ComplicatePropertyAnimation extends Activity{
    private ImageView mImageView = null;
    private Button mButton = null;
    private Button mButton2 = null;
    private Button mButton3 = null;
    private Button mButton4 = null;
    private AnimatorSet set = null;
    private ObjectAnimator objectAnimator = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complicatepropertyanim);
		mImageView = (ImageView)findViewById(R.id.mImageView);
		mImageView.setImageResource(R.drawable.ali);
		mButton = (Button)findViewById(R.id.button1);
	    mButton2 = (Button)findViewById(R.id.button2);
	    mButton3 = (Button)findViewById(R.id.button3);
	    mButton3 = (Button)findViewById(R.id.button3);
	    
	    mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				complicatePropertyAnim(mImageView);
			}
		});
	    mButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PropertyValuesHolderAnim(mImageView);				
			}
		});
	    mButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				KeyFrameAnim(mImageView);
			}
		});
	    
	}
	//1. 属性动画通过XML实现
	public void complicatePropertyAnim(View view) {
		set = (AnimatorSet)AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.complicatepropertyanim);
	    set.setTarget(view);
	    set.start(); 
	}
	//3. 属性动画通过PropertyValuesHolder实现
	public void PropertyValuesHolderAnim(View view) {
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofFloat("rotation", 0.0f, 180.0f);
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 2.0f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 2.0f);
	    PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofFloat("x", 0.0f, 100.0f);
	    PropertyValuesHolder pvhTranslateY = PropertyValuesHolder.ofFloat("y", 0.0f, 100.0f);
	    objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhAlpha, pvhRotation, 
	    		                                               pvhScaleX, pvhScaleY, 
	    		                                               pvhTranslateX, pvhTranslateY);
	    objectAnimator.setDuration(1000);
	    objectAnimator.start();
	}
    //3. 属性动画的关键类的使用 KeyFrames
    public void KeyFrameAnim(View view) {
    	Keyframe kf0 = Keyframe.ofFloat(0, 0.0f);
    	Keyframe kf1 = Keyframe.ofFloat(0.25f, 45.0f);
    	Keyframe kf2 = Keyframe.ofFloat(0.5f, 90.0f);
    	Keyframe kf4 = Keyframe.ofFloat(0.75f, 135.0f);
    	Keyframe kf3 = Keyframe.ofFloat(1f, 180.0f);
    	PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, 
    			                                                          kf1, kf2, kf4, kf3);
    	ObjectAnimator rotationAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhRotation);
    	rotationAnim.setDuration(2000);
    	rotationAnim.start();
    }

}
