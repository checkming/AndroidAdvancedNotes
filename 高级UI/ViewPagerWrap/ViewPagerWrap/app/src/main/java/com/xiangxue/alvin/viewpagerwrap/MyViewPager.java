package com.xiangxue.alvin.viewpagerwrap;

import android.content.Context;
import android.icu.util.Measure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.viewpagerwrap
 * @fileName MyViewPager
 * @date on 2019/7/3
 * @qq 2464061231
 **/
public class MyViewPager extends ViewPager {
    private static final String TAG = "MyViewPager";
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
            Log.d(TAG, "onMeasure: "  + h);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
