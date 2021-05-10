package com.arch.demo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.arch.demo.main.MainActivity;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
/**
 * 利用给SplashActivity设置带有背景的SplashTheme来避免启动白屏的问题
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
