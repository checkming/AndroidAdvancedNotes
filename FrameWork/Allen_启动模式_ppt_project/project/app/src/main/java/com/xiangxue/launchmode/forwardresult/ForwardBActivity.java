package com.xiangxue.launchmode.forwardresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ForwardBActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ForwardCActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

}