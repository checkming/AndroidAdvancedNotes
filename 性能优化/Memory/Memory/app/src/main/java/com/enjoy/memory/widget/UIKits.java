package com.enjoy.memory.widget;

import android.content.Context;

public class UIKits {
    /**
     * @param context
     * @param dip
     * @return
     */
    public static int dip2Px(Context context, float dip) {
        if (dip < 0) {
            return (int) (-dip * context.getResources().getDisplayMetrics().density + 0.5f) * -1;
        }
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

}