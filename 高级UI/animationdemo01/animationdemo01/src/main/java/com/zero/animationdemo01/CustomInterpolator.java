package com.zero.animationdemo01;

import android.view.animation.Interpolator;

public class CustomInterpolator implements Interpolator{
    private int LinearInterpolator = 0;
    private int AccelerateInterpolator = 1;
    private int factorA = 2;
	@Override
	public float getInterpolation(float arg0) {
		// TODO Auto-generated method stub
		int interpolatorType = AccelerateInterpolator;
		if (interpolatorType == LinearInterpolator)
		{
		    // 匀速运动插值器 s = v * t	
			return 2 * arg0;
		}
		else if (interpolatorType == AccelerateInterpolator)
		{
			// 匀加速插值器 s = a/2 * t * t
			return factorA / 2 * arg0 * arg0;
		}
		return 0;
	}

}
