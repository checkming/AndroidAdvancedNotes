package com.xiangxue.launchmode.task;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TaskView extends LinearLayout {

    private Context mContext;

    public TaskView(Context context) {
        this(context, null);
    }

    public TaskView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        setLayoutParams(layoutParams);
    }

    // Function
    public void set(int taskId, List<Integer> histIdList) {
        //
        TextView taskIdTv = generate(String.format("Task id: %d", taskId));
        addView(taskIdTv);

        if (histIdList != null) {
            for (int histId : histIdList) {
                TextView histIdTv = generate(String.valueOf(histId));
                addView(histIdTv);
            }
        }
    }

    // Internal
    private TextView generate(String str) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(mContext);
        tv.setText(str);
        tv.setLayoutParams(layoutParams);

        return tv;
    }
}
