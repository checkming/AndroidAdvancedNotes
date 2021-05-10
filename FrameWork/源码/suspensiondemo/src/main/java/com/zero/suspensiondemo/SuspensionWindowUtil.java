package com.zero.suspensiondemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;


public class SuspensionWindowUtil {

    private SuspensionView suspensionView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Context mContext;
    private ValueAnimator valueAnimator;
    private int direction;
    private final int LEFT = 0;
    private final int RIGHT = 1;

    //私有化构造函数
    public SuspensionWindowUtil(Context context) {
        mContext = context;
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void showSuspensionView() {

        //创建suspensionview
        suspensionView = new SuspensionView(mContext);

        //创建一个布局参数
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width =suspensionView.width;
        layoutParams.height = suspensionView.height;

        //gravity

        layoutParams.x += ScreenSizeUtil.getScreenWidth();
        layoutParams.y += ScreenSizeUtil.getScreenHeight() - ScreenSizeUtil.dp2px(10);
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        //设置window type
        //悬浮窗口
        //系统级别 type
        //应用级别
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        //flags
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        layoutParams.format = PixelFormat.RGBA_8888;

        suspensionView.setOnTouchListener(touchListener);

        //WindowManager 通过WindowManager添加view
        windowManager.addView(suspensionView,layoutParams);


    }


    public void hideSuspensionView() {
        if(suspensionView !=null){
            windowManager.removeView(suspensionView);
            stopAnim();
        }
    }


    View.OnTouchListener touchListener = new View.OnTouchListener() {
        float startX;
        float startY;
        float moveX;
        float moveY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();

                    moveX = event.getRawX();
                    moveY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX() - moveX;
                    float y = event.getRawY() - moveY;
                    //计算偏移量，刷新视图
                    layoutParams.x += x;
                    layoutParams.y += y;
                    windowManager.updateViewLayout(suspensionView, layoutParams);
                    moveX = event.getRawX();
                    moveY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    //判断松手时View的横坐标是靠近屏幕哪一侧，将View移动到依靠屏幕
                    float endX = event.getRawX();
                    float endY = event.getRawY();
                    if (endX < ScreenSizeUtil.getScreenWidth() / 2) {
                        direction = LEFT;
                        endX = 0;
                    } else {
                        direction = RIGHT;
                        endX = ScreenSizeUtil.getScreenWidth() - suspensionView.width;
                    }
                    if (moveX != startX) {
                        starAnim((int) moveX, (int) endX, direction);
                    }
                    //如果初始落点与松手落点的坐标差值超过5个像素，则拦截该点击事件
                    //否则继续传递，将事件交给OnClickListener函数处理
                    if (Math.abs(startX - moveX) > 5) {
                        return true;
                    }
                    break;
            }
            return false;
        }
    };


    private void starAnim(int startX, int endX, final int direction) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
        valueAnimator = ValueAnimator.ofInt(startX, endX);
        valueAnimator.setDuration(500);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (direction == LEFT) {
                    layoutParams.x = (int) animation.getAnimatedValue() - suspensionView.width / 2;
                } else {
                    layoutParams.x = (int) animation.getAnimatedValue();
                }
                if (suspensionView != null) {
                    windowManager.updateViewLayout(suspensionView, layoutParams);
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void stopAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }
}
