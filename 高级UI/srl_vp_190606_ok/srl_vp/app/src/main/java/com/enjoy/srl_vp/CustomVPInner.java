package com.enjoy.srl_vp;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomVPInner extends ViewPager {

    private float startX;
    private float startY;
    private float x;
    private float y;
    private float deltaX;
    private float deltaY;

    public CustomVPInner(Context context) {
        super(context);
    }

    public CustomVPInner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("leo", "dispatchTouchEvent: Down");
                startX = ev.getX();
                startY = ev.getY();
//                ViewCompat.setNestedScrollingEnabled(this,true);
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("leo", "dispatchTouchEvent: Move");
                x = ev.getX();
                y = ev.getY();
                deltaX = Math.abs(x - startX);
                deltaY = Math.abs(y - startY);
                if (deltaX < deltaY) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        boolean a = super.dispatchTouchEvent(ev);
        Log.e("leo", "dispatchTouchEvent: a = "+a);

        return a;
    }

}
