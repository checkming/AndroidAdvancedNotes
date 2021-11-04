package com.enjoy.bigimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

public class BigImageView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {


    Rect mRect;
    BitmapFactory.Options mOptions;
    int mImageWidth;
    int mImageHeight;
    BitmapRegionDecoder mBitmapRegionDecoder;
    private GestureDetector mGestureDetector;
    private Scroller mScroller;

    public BigImageView(Context context) {
        this(context, null, 0);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRect = new Rect();
        mOptions = new BitmapFactory.Options();

        //手势
        mGestureDetector = new GestureDetector(context, this);
        // 将触摸事件交给手势处理
        setOnTouchListener(this);
        // 滑动帮助
        mScroller = new Scroller(context);

    }

    public void setImage(InputStream is) {

        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, mOptions);

        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;

        mOptions.inMutable = true;
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        mOptions.inJustDecodeBounds = false;

        try {
            // false 不共享 图片源
            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();
    }

    int mViewHeight;
    int mViewWidth;
    float mScale;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();

        if (mBitmapRegionDecoder == null) {
            return;
        }

        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth;

        // 缩放因子
        mScale = mViewWidth / (float) mImageWidth;

        // x * mscale = mViewHeight
        mRect.bottom = (int) (mViewHeight / mScale);

        // 第一种方式优化
//        mOptions.inSampleSize = calcuteInSampleSize(mImageWidth, mImageHeight, mViewWidth, mViewHeight);

        // 第二种方式优化
//        float temp = 1.0f / mScale;
//        if (temp > 1) {
//            mOptions.inSampleSize = (int) Math.pow(2, (int) (temp));
//        } else {
//            mOptions.inSampleSize = 1;
//        }


        Log.e("Leo", "============缩放后=========");
        Log.e("Leo", "inSampleSize = " + mOptions.inSampleSize);
        Log.e("Leo", "mScale = " + mScale);
        Log.e("Leo", "图片宽 = " + mImageWidth + ",高 = " + mImageHeight);
        Log.e("Leo", "view 宽 = " + mViewWidth + ",高 = " + mViewHeight);

    }

    /**
     * @param w    图片宽
     * @param h    图片高
     * @param maxW View 宽
     * @param maxH View 高
     * @return
     */
    private static int calcuteInSampleSize(int w, int h, int maxW, int maxH) {

        int inSampleSize = 1;

        if (w > maxW && h > maxH) {
            inSampleSize = 2;

            while (w / inSampleSize > maxW && h / inSampleSize > maxH) {
                inSampleSize *= 2;
            }

        }

        return inSampleSize;
    }

    Bitmap bitmap = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmapRegionDecoder == null) {
            return;
        }

        mOptions.inBitmap = bitmap;
        bitmap = mBitmapRegionDecoder.decodeRegion(mRect, mOptions);
        Log.e("leo", "图片大小 " + bitmap.getByteCount());// 没有优化：44338752，1.优化：2770200，2优化：692064

        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
//        matrix.setScale(mScale * mOptions.inSampleSize, mScale * mOptions.inSampleSize);

        canvas.drawBitmap(bitmap, matrix, null);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // 如果滑动还没有停止 强制停止
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        //继续接收后续事件
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //改变加载图片的区域
        mRect.offset(0, (int) distanceY);

        //bottom大于图片高了， 或者 top小于0了
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - (int) (mViewHeight / mScale);
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int) (mViewHeight / mScale);
        }

        // 重绘
        invalidate();
        return false;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /**
         * startX: 滑动开始的x坐标
         * velocityX: 以每秒像素为单位测量的初始速度
         * minX: x方向滚动的最小值
         * maxX: x方向滚动的最大值
         */
        mScroller.fling(0, mRect.top, 0, (int) -velocityY, 0, 0,
                0, mImageHeight - (int) (mViewHeight / mScale));
        return false;
    }

    /**
     * 获取计算结果并且重绘
     */
    @Override
    public void computeScroll() {
        //已经计算结束 return
        if (mScroller.isFinished()) {
            return;
        }
        //true 表示当前动画未结束
        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int) (mViewHeight / mScale);
            invalidate();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 事件交给手势处理
        return mGestureDetector.onTouchEvent(event);
    }
}
