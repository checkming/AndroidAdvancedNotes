package com.enjoy.memory.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.BlockingQueue;

public class IOSStyleLoadingView extends View {

    private final Context context;
    private double radius;
    private double insideRadius;
    private float northwestXStart;
    private float northwestYStart;
    private float northXStart;
    private float northYStart;
    private float notheastXStart;
    private float notheastYStart;
    private float eastXStart;
    private float eastYStart;
    private float southeastXStart;
    private float southeastYStart;
    private float southXStart;
    private float southYStart;
    private float southwestXStart;
    private float southwestYStart;
    private float westXStart;
    private float westYStart;

    private float northwestXEnd;
    private float northwestYEnd;
    private float northXEnd;
    private float northYEnd;
    private float notheastXEnd;
    private float notheastYEnd;
    private float eastXEnd;
    private float eastYEnd;
    private float southeastXEnd;
    private float southeastYEnd;
    private float southXEnd;
    private float southYEnd;
    private float southwestXEnd;
    private float southwestYEnd;
    private float westXEnd;
    private float westYEnd;

    private int currentColor = 7;

    String color[] = new String[]{
            "#a5a5a5",
            "#b7b7b7",
            "#c0c0c0",
            "#c9c9c9",
            "#d2d2d2",
            "#dbdbdb",
            "#e4e4e4",
            "#e4e4e4"
    };

    int[] colors = new int[8];


    public IOSStyleLoadingView(Context context) {
        this(context, null, 0);
    }

    public IOSStyleLoadingView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public IOSStyleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        radius = UIKits.dip2Px(context, 9);
        insideRadius = UIKits.dip2Px(context, 5);

        for (int i = 0; i < color.length; i++) {
            colors[i] = Color.parseColor(color[i]);
        }

        paint.setAntiAlias(true);
        paint.setStrokeWidth(UIKits.dip2Px(context, 2));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    Path path = new Path();
    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(northwestXStart, northwestYStart);
        path.lineTo(northwestXEnd, northwestYEnd);
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(northXStart, northYStart);
        path.lineTo(northXEnd, northYEnd);
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(notheastXStart, notheastYStart);
        path.lineTo(notheastXEnd, notheastYEnd);
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(eastXStart, eastYStart);
        path.lineTo(eastXEnd, eastYEnd);
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(southeastXStart, southeastYStart);
        path.lineTo(southeastXEnd, southeastYEnd);
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(southXStart, southYStart);
        path.lineTo(southXEnd, southYEnd);
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(southwestXStart, southwestYStart);
        path.lineTo(southwestXEnd, southwestYEnd);
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(colors[(currentColor++) % 8]);
        path.moveTo(westXStart, westYStart);
        path.lineTo(westXEnd, westYEnd);
        canvas.drawPath(path, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        double centerX = getMeasuredWidth() / 2;
        double centerY = getMeasuredHeight() / 2;
        double leg = radius * Math.cos(Math.PI / 4);
        double insideLeg = insideRadius * Math.cos(Math.PI / 4);


        northwestXStart = (float) (centerX - leg);
        northwestYStart = (float) (centerY - leg);
        northXStart = (float) centerX;
        northYStart = (float) (centerY - radius);
        notheastXStart = (float) (centerX + leg);
        notheastYStart = (float) (centerY - leg);
        eastXStart = (float) (centerX + radius);
        eastYStart = (float) centerY;
        southeastXStart = (float) (centerX + leg);
        southeastYStart = (float) (centerY + leg);
        southXStart = (float) centerX;
        southYStart = (float) (centerY + radius);
        southwestXStart = (float) (centerX - leg);
        southwestYStart = (float) (centerY + leg);
        westXStart = (float) (centerX - radius);
        westYStart = (float) centerY;

        northwestXEnd = (float) (centerX - insideLeg);
        northwestYEnd = (float) (centerY - insideLeg);
        northXEnd = (float) centerX;
        northYEnd = (float) (centerY - insideRadius);
        notheastXEnd = (float) (centerX + insideLeg);
        notheastYEnd = (float) (centerY - insideLeg);
        eastXEnd = (float) (centerX + insideRadius);
        eastYEnd = (float) centerY;
        southeastXEnd = (float) (centerX + insideLeg);
        southeastYEnd = (float) (centerY + insideLeg);
        southXEnd = (float) centerX;
        southYEnd = (float) (centerY + insideRadius);
        southwestXEnd = (float) (centerX - insideLeg);
        southwestYEnd = (float) (centerY + insideLeg);
        westXEnd = (float) (centerX - insideRadius);
        westYEnd = (float) centerY;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
        }
    }

    private ValueAnimator valueAnimator;

    public void startAnimation() {
        valueAnimator = ValueAnimator.ofInt(7, 0);
        valueAnimator.setDuration(400);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if ((int) animation.getAnimatedValue() != currentColor) {
//                    String b[] = new String[color.length];//移动后的数组
//                    for (int c = 0, size = color.length - 1; c < size; c++) {
//                        b[c + 1] = color[c];
//                    }
//                    b[0] = color[color.length - 1];
//                    color = b;
                    invalidate();
                    currentColor = (int) animation.getAnimatedValue();
                }
            }
        });
        valueAnimator.start();
    }

}
