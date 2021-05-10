package com.xiangxue.launchmode.forwardresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ForwardCActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        intent.putExtra("a", 28);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

