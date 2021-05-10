package com.zero.animationdemo01;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;



public  abstract class BaseActivity extends Activity{
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setView();
		initView();
		setListener();
	}
	

	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 设置布局文件
	 */
	public abstract void setView();

	/**
	 * 初始化布局文件中的控件
	 */
	public abstract void initView();

	/**
	 * 设置控件的监听
	 */
	public abstract void setListener();
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}



	
	
}
