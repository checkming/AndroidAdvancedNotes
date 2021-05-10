package com.enjoy.srl_vp;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public class CustomSRL2 extends SwipeRefreshLayout {
    public CustomSRL2(Context context) {
        super(context);
    }

    public CustomSRL2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    // 能不能直接调用到ViewGroup
    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        Class clazz = ViewGroup.class;

        try {
            Field mGroupFlagsField =  clazz.getDeclaredField("mGroupFlags");
            mGroupFlagsField.setAccessible(true);
            int c = (int) mGroupFlagsField.get(this);
            Log.e("leo", "dispatchTouchEvent: c " + c);
            if (b) {
                mGroupFlagsField.set(this, 2900051);
            } else {
                mGroupFlagsField.set(this, 2245715);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        super.requestDisallowInterceptTouchEvent(b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.e("leo", "onInterceptTouchEvent: "+ev.getAction());

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            super.onInterceptTouchEvent(ev);
            return false;
        }
        return true;
    }

}
