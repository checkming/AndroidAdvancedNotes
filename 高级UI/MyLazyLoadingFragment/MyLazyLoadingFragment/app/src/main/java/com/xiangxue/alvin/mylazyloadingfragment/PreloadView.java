package com.xiangxue.alvin.mylazyloadingfragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class PreloadView extends View {

    private Paint mTextPaint;

    private String mText;

    private int mTextColor;

    public PreloadView(Context context) {
        super(context);
        init();
    }

    public PreloadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PreloadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PreloadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置字体颜色
        mTextPaint.setColor(mTextColor);
        //设置字体大小
        mTextPaint.setTextSize(200f);
        //设置线条宽度;
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(20f);

        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
//        Log.i("Zero", "onDraw, text: " + mText + ", width: " + width + ", heigth: " + height);
        canvas.drawText(mText, width / 2, height / 2, mTextPaint);

    }
}
