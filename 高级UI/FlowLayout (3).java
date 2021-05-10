/*
 * Copyright 2013 Blaz Solar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zero.flowlayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import androidx.core.view.ViewConfigurationCompat;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private static final String TAG = "Zero";

    private List<View> lineViews;//每一行的子View
    private List<List<View>> views;//所有的行 一行一行的存储
    private List<Integer> heights;//每一行的高度

    private boolean scrollable = false;
    private int measureHeight;//代表本身的测量高度
    private int realHeight;//表示内容的高度

    private int mTouchSlop;//用来判断是不是一次滑动
    private float mLastInterceptX = 0;
    private float mLastInterceptY = 0;
    private float mLastY = 0;

    private OverScroller mScroller;

    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private int mOverscrollDistance;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);//获取最小滑动距离
        Log.i(TAG, "FlowLayout: mTouchSlop= " + mTouchSlop);
        mScroller = new OverScroller(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
    }


    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void init() {
        views = new ArrayList<>();
        lineViews = new ArrayList<>();
        heights = new ArrayList<>();
    }

    private String getModeString(int mode) {
        String result = "Unkown";
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                result = "UNSPECIFIED";
                break;
            case MeasureSpec.EXACTLY:
                result = "EXACTLY";
                break;
            case MeasureSpec.AT_MOST:
                result = "AT_MOST";
                break;
            default:
        }
        return result;
    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = measureHeight;
            int bottom = realHeight;

            mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
                    Math.max(0, bottom - height), 0, height / 2);

            postInvalidateOnAnimation();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        float xInterceptX = ev.getX();
        float yInterceptY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastInterceptX = xInterceptX;
                mLastInterceptY = yInterceptY;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = xInterceptX - mLastInterceptX;
                float dy = yInterceptY - mLastInterceptY;
                if (Math.abs(dy) > Math.abs(dx) && Math.abs(dy) > mTouchSlop) {
                    intercepted = true;//表示本身需要拦截处理
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        mLastInterceptX = xInterceptX;
        mLastInterceptY = yInterceptY;

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//处理滑动
        if (!scrollable) {
            return super.onTouchEvent(event);
        }
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        float currY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastY = currY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = mLastY - currY;//本次手势滑动了多大距离
                int oldScrollY = getScrollY();//已经偏移了的距离
//                int scrollY = oldScrollY + (int)dy;//这是本次需要偏移的距离 = 之前已经偏移了的距离 + 本次手势滑动的距离
//                if(scrollY < 0){
//                    scrollY = 0;
//                }
//                if(scrollY > realHeight - measureHeight){
//                    scrollY = realHeight - measureHeight;
//                }
//                scrollTo(0,scrollY);
                Log.i(TAG, "onTouchEvent: dy= " + dy);
                mScroller.startScroll(0, mScroller.getFinalY(), 0, (int) dy);//mCurrY = oldScrollY + dy*scale;
                invalidate();
                mLastY = currY;
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();

                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(-initialVelocity);
                } else if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0,
                        (realHeight - measureHeight))) {
                    postInvalidateOnAnimation();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {//mCurrY = oldScrollY + dy*scale;
            //int scrollY = oldScrollY + (int)dy;
//            int currY = mScroller.getCurrY();
//            if(currY < 0){
//                currY = 0;
//            }
//            if(currY > realHeight - measureHeight){
//                currY = realHeight - measureHeight;
//            }
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure: heightMode= " + getModeString(heightMode));
        measureHeight = heightSize;
        //记录当前行的宽度和高度
        int lineWidth = 0;// 宽度是当前行子view的宽度之和
        int lineHeight = 0;// 高度是当前行所有子View中高度的最大值

        //整个流式布局的宽度和高度
        int flowlayoutWidth = 0;//所有行中宽度的最大值
        int flowlayoutHeight = 0;// 所以行的高度的累加

        //初始化参数列表
        init();

        //遍历所有的子View，对子View进行测量，分配到具体的行
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View child = this.getChildAt(i);
            //测量子View 获取到当前子View的测量的宽度/高度
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //获取到当前子View的测量的宽度/高度
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
//            Log.i(TAG, "onMeasure: childHeight= " + childHeight);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            //看下当前的行的剩余的宽度是否可以容纳下一个子View,
            // 如果放不下，换行 保存当前行的所有子View,累加行高，当前的宽度，高度 置零
            if (lineWidth + childWidth > widthSize) {//换行
                if (lineViews.size() == 1 && lineViews.get(0).getLayoutParams().height == LayoutParams.MATCH_PARENT) {//如果一行只有一个元素
                    lineHeight = Utils.dp2px(150);
                }
                views.add(lineViews);
                lineViews = new ArrayList<>();//创建新的一行
                flowlayoutWidth = Math.max(flowlayoutWidth, lineWidth);
                flowlayoutHeight += lineHeight;
                heights.add(lineHeight);
                lineWidth = 0;
                lineHeight = 0;
            }
            lineViews.add(child);
            lineWidth += childWidth;
            if (lp.height != LayoutParams.MATCH_PARENT) {//暂时先不处理layout_heigth = match_parent
                lineHeight = Math.max(lineHeight, childHeight);
            }
//            Log.i(TAG, "onMeasure: " + lineHeight);

            if (i == childCount - 1) {//最后一行
                flowlayoutHeight += lineHeight;
                flowlayoutWidth = Math.max(flowlayoutWidth, lineWidth);
                heights.add(lineHeight);
                views.add(lineViews);
            }
        }
        //重新测量一次layout_heigth = match_parent
        remeasureChild(widthMeasureSpec, heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            flowlayoutHeight = Math.max(heightSize, flowlayoutHeight);
        }

        realHeight = flowlayoutHeight;
        scrollable = realHeight > measureHeight;

        //FlowLayout最终宽高
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : flowlayoutWidth
                , realHeight);


    }

    private void remeasureChild(int widthMeasureSpec, int heightMeasureSpec) {
        int lineSize = views.size();
        for (int i = 0; i < lineSize; i++) {
            int lineHeight = heights.get(i);//每一行行高
//            Log.i(TAG, "remeasureChild: " + lineHeight);
            List<View> lineViews = views.get(i);//每一行的子View
            int size = lineViews.size();

            for (int j = 0; j < size; j++) {
                View child = lineViews.get(j);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {

                    int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
                    int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lineHeight);
                    child.measure(childWidthSpec, childHeightSpec);

                }
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int lineCount = views.size();

        int currX = 0;
        int currY = 0;

        for (int i = 0; i < lineCount; i++) {//大循环，所有的子View 一行一行的布局
            List<View> lineViews = views.get(i);//取出一行
            int lineHeight = heights.get(i);// 取出这一行的高度值
            //遍历当前行的子View
            int size = lineViews.size();
            for (int j = 0; j < size; j++) {//布局当前行的每一个view
                View child = lineViews.get(j);
                int left = currX;
                int top = currY;
                int right = left + child.getMeasuredWidth();
                int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
                //确定下一个view的left
                currX += child.getMeasuredWidth();
            }
            currY += lineHeight;
            currX = 0;
        }

    }


    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && p instanceof LayoutParams;
    }


    public static class LayoutParams extends MarginLayoutParams {

        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);
            try {
                gravity = a.getInt(R.styleable.FlowLayout_Layout_android_layout_gravity, -1);
            } finally {
                a.recycle();
            }


        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        @Override
        public String toString() {
            return "LayoutParams{" +
                    "gravity=" + gravity +
                    ", bottomMargin=" + bottomMargin +
                    ", leftMargin=" + leftMargin +
                    ", rightMargin=" + rightMargin +
                    ", topMargin=" + topMargin +
                    ", height=" + height +
                    ", width=" + width +
                    "} ";
        }
    }

}
