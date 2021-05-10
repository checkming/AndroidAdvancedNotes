package com.zero.suspensiondemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


public class SuspensionView extends View {
    private static final String TAG = "Zero";

    public int width;

    public int height;

    private Bitmap bitmap;

    public SuspensionView(Context context) {
        super(context);
        init();
    }

    public SuspensionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SuspensionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.katou);
        width = ScreenSizeUtil.dp2px(66.6f);
        height = width;
        bitmap = Bitmap.createScaledBitmap(src, width, height, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
