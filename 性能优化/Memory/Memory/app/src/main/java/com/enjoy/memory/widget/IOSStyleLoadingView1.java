package com.enjoy.memory.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class IOSStyleLoadingView1 extends View {

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

    public IOSStyleLoadingView1(Context context) {
        this(context,null,0);
    }

    public IOSStyleLoadingView1(Context context, AttributeSet attrs) {
        this(context,null,0);
    }

    public IOSStyleLoadingView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        radius = UIKits.dip2Px(context, 9);
        insideRadius = UIKits.dip2Px(context, 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(UIKits.dip2Px(context, 2));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Path p0 = new Path();
        paint.setColor(Color.parseColor(color[0]));
        p0.moveTo(northwestXStart, northwestYStart);
        p0.lineTo(northwestXEnd, northwestYEnd);
        canvas.drawPath(p0, paint);

        Path p1 = new Path();
        paint.setColor(Color.parseColor(color[1]));
        p1.moveTo(northXStart, northYStart);
        p1.lineTo(northXEnd, northYEnd);
        canvas.drawPath(p1, paint);

        Path p2 = new Path();
        paint.setColor(Color.parseColor(color[2]));
        p2.moveTo(notheastXStart, notheastYStart);
        p2.lineTo(notheastXEnd, notheastYEnd);
        canvas.drawPath(p2, paint);

        Path p3 = new Path();
        paint.setColor(Color.parseColor(color[3]));
        p3.moveTo(eastXStart, eastYStart);
        p3.lineTo(eastXEnd, eastYEnd);
        canvas.drawPath(p3, paint);

        Path p4 = new Path();
        paint.setColor(Color.parseColor(color[4]));
        p4.moveTo(southeastXStart, southeastYStart);
        p4.lineTo(southeastXEnd, southeastYEnd);
        canvas.drawPath(p4, paint);

        Path p5 = new Path();
        paint.setColor(Color.parseColor(color[5]));
        p5.moveTo(southXStart, southYStart);
        p5.lineTo(southXEnd, southYEnd);
        canvas.drawPath(p5, paint);

        Path p6 = new Path();
        paint.setColor(Color.parseColor(color[6]));
        p6.moveTo(southwestXStart, southwestYStart);
        p6.lineTo(southwestXEnd, southwestYEnd);
        canvas.drawPath(p6, paint);

        Path p7 = new Path();
        paint.setColor(Color.parseColor(color[7]));
        p7.moveTo(westXStart, westYStart);
        p7.lineTo(westXEnd, westYEnd);
        canvas.drawPath(p7, paint);

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
                    String b[] = new String[color.length];//移动后的数组
                    for (int c = 0, size = color.length - 1; c < size; c++) {
                        b[c + 1] = color[c];
                    }
                    b[0] = color[color.length - 1];
                    color = b;
                    invalidate();
                    currentColor = (int) animation.getAnimatedValue();
                }
            }
        });
        valueAnimator.start();
    }

}
