package com.xiangxue.launchmode;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xiangxue.launchmode.task.TaskData;
import com.xiangxue.launchmode.task.TaskView;

import java.util.List;
import java.util.Map;

public class BaseActivity extends Activity implements View.OnClickListener {

    private static int sId = 0;

    private HorizontalScrollView mHsv;
    private LinearLayout mTaskLl;

    private Button mStartActivityBtn;
    private TextView mIdTv;
    private Button mExitBtn;

    private RadioButton mModeStandardA;
    private RadioButton mModeStandardB;
    private RadioButton mModeStandardC;
    private RadioButton mModeSingleTopA;
    private RadioButton mModeSingleTopB;
    private RadioButton mModeSingleTopC;
    private RadioButton mModeSingleTaskA;
    private RadioButton mModeSingleTaskB;
    private RadioButton mModeSingleTaskC;
    private RadioButton mModeSingleInstanceA;
    private RadioButton mModeSingleInstanceB;
    private RadioButton mModeSingleInstanceC;

    private CheckBox mFlagNewTask;
    private CheckBox mFlagNewDocument;
    private CheckBox mFlagMultipleTask;
    private CheckBox mFlagClearTask;
    private CheckBox mFlagTaskOnHome;
    private CheckBox mFlagSingleTop;
    private CheckBox mFlagClearTop;
    private CheckBox mFlagNoHistory;
    private CheckBox mFlagExcludeFromRecents;
    private CheckBox mFlagRetainInRecents;
    private CheckBox mFlagReorderToFront;
    private CheckBox mFlagPreviousIsTop;

    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(getTAG(), "--onCreate--");

        mHsv = $(R.id.hsv_task);
        mTaskLl = $(R.id.ll_task);

        mStartActivityBtn = $(R.id.btn_start_activity);
        mIdTv = $(R.id.tv_id);
        mExitBtn = $(R.id.btn_exit);

        mModeStandardA = $(R.id.rb_mode_standard_a);
        mModeStandardB = $(R.id.rb_mode_standard_b);
        mModeStandardC = $(R.id.rb_mode_standard_c);
        mModeSingleTopA = $(R.id.rb_mode_single_top_a);
        mModeSingleTopB = $(R.id.rb_mode_single_top_b);
        mModeSingleTopC = $(R.id.rb_mode_single_top_c);
        mModeSingleTaskA = $(R.id.rb_mode_single_task_a);
        mModeSingleTaskB = $(R.id.rb_mode_single_task_b);
        mModeSingleTaskC = $(R.id.rb_mode_single_task_c);
        mModeSingleInstanceA = $(R.id.rb_mode_single_instance_a);
        mModeSingleInstanceB = $(R.id.rb_mode_single_instance_b);
        mModeSingleInstanceC = $(R.id.rb_mode_single_instance_c);

        mFlagNewTask = $(R.id.cb_flag_new_task);
        mFlagNewDocument = $(R.id.cb_flag_new_document);
        mFlagMultipleTask = $(R.id.cb_flag_multiple_task);
        mFlagClearTask = $(R.id.cb_flag_clear_task);
        mFlagTaskOnHome = $(R.id.cb_flag_task_on_home);
        mFlagSingleTop = $(R.id.cb_flag_single_top);
        mFlagClearTop = $(R.id.cb_flag_clear_top);
        mFlagNoHistory = $(R.id.cb_flag_no_history);
        mFlagExcludeFromRecents = $(R.id.cb_flag_exclude_from_recents);
        mFlagRetainInRecents = $(R.id.cb_flag_retain_in_recents);
        mFlagReorderToFront = $(R.id.cb_flag_reorder_to_front);
        mFlagPreviousIsTop = $(R.id.cb_flag_previous_is_top);

        //
        mId = sId++;
        mStartActivityBtn.setOnClickListener(this);
        mIdTv.setText(String.format("id: %d", mId));
        mExitBtn.setOnClickListener(this);

        //
        TaskData.getInstance().add(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_activity:
                startActivity(parseIntent());
                break;

            case R.id.btn_exit: {
                // TODO: impl
                Toast.makeText(this, "TODO: not impl", Toast.LENGTH_SHORT).show();
                break;
            }

            default:
                break;
        }
    }

    // Function
    public int getId() {
        return mId;
    }

    // Internal
    private Intent parseIntent() {
        Intent intent = new Intent();

        // Launch mode
        if (false) {
            // Stub

        } else if (mModeStandardA.isChecked()) {
            intent.setComponent(new ComponentName(this, StandardAActivity.class));
        } else if (mModeStandardB.isChecked()) {
            intent.setComponent(new ComponentName(this, StandardBActivity.class));
        } else if (mModeStandardC.isChecked()) {
            intent.setComponent(new ComponentName(this, StandardCActivity.class));

        } else if (mModeSingleTopA.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleTopAActivity.class));
        } else if (mModeSingleTopB.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleTopBActivity.class));
        } else if (mModeSingleTopC.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleTopCActivity.class));

        } else if (mModeSingleTaskA.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleTaskAActivity.class));
        } else if (mModeSingleTaskB.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleTaskBActivity.class));
        } else if (mModeSingleTaskC.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleTaskCActivity.class));

        } else if (mModeSingleInstanceA.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleInstanceAActivity.class));
        } else if (mModeSingleInstanceB.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleInstanceBActivity.class));
        } else if (mModeSingleInstanceC.isChecked()) {
            intent.setComponent(new ComponentName(this, SingleInstanceCActivity.class));

        } else {
            // Nothing
        }

        // Flags
        if (mFlagNewTask.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (mFlagNewDocument.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        if (mFlagMultipleTask.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        if (mFlagClearTask.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        if (mFlagTaskOnHome.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        }
        if (mFlagSingleTop.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        if (mFlagClearTop.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (mFlagNoHistory.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }

        if (mFlagExcludeFromRecents.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }
        if (mFlagRetainInRecents.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        }
        if (mFlagReorderToFront.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        if (mFlagPreviousIsTop.isChecked()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        }

        return intent;
    }

    private void drawTask() {
        TaskData taskData = TaskData.getInstance();
        for (Map.Entry<Integer, List<Integer>> kv : taskData.getData().entrySet()) {
            TaskView taskView = new TaskView(this);
            taskView.set(kv.getKey(), kv.getValue());

            mTaskLl.addView(taskView);
        }
    }

    private <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.i("BaseActivity", "onNewIntent called.");
    }
    @Override
    protected void onRestart() {
        Log.d(getTAG(), "--onRestart--");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(getTAG(), "--onStart--");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(getTAG(), "--onPause--");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(getTAG(), "--onStop--");
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getTAG(), "--onResume--");

        // Must disconnectAll before setting closeOnDisconnectAll to know whether to keep the
        // activity open after disconnecting.
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            Log.d(getTAG(), "FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY is set.");
        }
        mTaskLl.removeAllViews();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawTask();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getTAG(), "--onDestroy--");

        TaskData.getInstance().remove(this);
    }

    protected String getTAG(){
        return "BaseActivity";
    }
}
