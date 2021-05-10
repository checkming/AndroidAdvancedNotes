package com.zero.animationdemo01;

import android.R.integer;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class SimplePropertyAnimation extends Activity{
    private ImageView mImageView = null;
    private Button mButton = null;
    private Button mButton3 = null;
    private AnimatorSet set = null;
    
    
    private static final int RED = 0xffFF8080;
    private static final int BLUE = 0xff8080FF;
    private static final int GREEN = 0xff80ff80;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simplepropertyanim);
		mImageView = (ImageView)findViewById(R.id.mImageView);
		mImageView.setImageResource(R.drawable.ali);
		mButton = (Button)findViewById(R.id.button1);
		mButton3 = (Button)findViewById(R.id.button3);
		//1.属性动画通过XML实现
		set = (AnimatorSet)AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.simplepropertyanim);
	    set.setTarget(mImageView);
	    
	    mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    set.start();
			}
		});
	    
	    mButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bgColorAnim(mImageView);
			}
		});
	    

	}
	
	//2. 属性动画通过代码实现
	public void scaleAnim(View view) {
		ObjectAnimator.ofFloat(view, "scaleX", 1, 2)
		                    .setDuration(1000)
		                    .start();
	}
	
	//3. 属性动画替换背景颜色
    public void bgColorAnim(View view) {
    	ObjectAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", GREEN, BLUE, RED);
        colorAnim.setTarget(view);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setDuration(3000);
        colorAnim.start();
    }
}
