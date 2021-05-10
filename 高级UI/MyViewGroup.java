package com.zero.myviewgroupdemo01;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {

    private static final int OFFSET = 100;//表示缩进的尺寸

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1. 测量自身
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 2. 为每个子View计算测量的限制信息 Mode / Size
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //3. 把上一步确定的限制信息，传递给每一个子View，然后子View开始measure
        //自己的尺寸
        int childCount = getChildCount();//子view的个数
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        }

        int width = 0;
        int height = 0;
        //4. 获取子View测量完成后的尺寸
        //5. ViewGroup根据自身的情况，计算自己的尺寸
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    int widthAddOffset = i * OFFSET + child.getMeasuredWidth();
                    width = Math.max(width, widthAddOffset);//取最大的宽度
                }
                break;
            default:
                break;
        }

        switch (heightMode){
            case MeasureSpec.EXACTLY:
                height =heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    height += child.getMeasuredHeight();
                }
                break;
            default:
                break;
        }
        //6. 保存自身的尺寸
        setMeasuredDimension(width,height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        1. 遍历子View for
//        2. 确定自己的规则
//        3. 子View的测量尺寸
//        4. left,top,right,bottom
//        6. child.layout
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = getChildAt(i);
            left  = i * OFFSET;
            right = left + child.getMeasuredWidth();
            bottom = top + child.getMeasuredHeight();
            child.layout(left,top,right,bottom);
            top += child.getMeasuredHeight();//不考虑padding margin gravity xxx

        }


    }
}
