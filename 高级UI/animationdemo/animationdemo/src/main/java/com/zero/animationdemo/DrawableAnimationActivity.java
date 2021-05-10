package com.zero.animationdemo;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DrawableAnimationActivity extends AppCompatActivity {

    private ImageView mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_animation);
        initToolbar();
        mTarget = findViewById(R.id.img_target);
    }

    private void initToolbar() {
        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_on_off, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        } else if (i == R.id.action_do) {
            doAnimation(getAnimationDrawable(false), true);
        } else if (i == R.id.action_stop) {
            doAnimation(getAnimationDrawable(true), false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void doAnimation(AnimationDrawable animationDrawable, boolean doIt) {
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }

        if (doIt) {
            animationDrawable.start();
        }
    }

    private AnimationDrawable getAnimationDrawable(boolean fromXml) {
        if (fromXml) {
            AnimationDrawable animationDrawable = (AnimationDrawable) mTarget.getDrawable();
            //animationDrawable.setOneShot(false);
            return animationDrawable;
        } else {
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (int i = 1; i < 8; i++) {
                int id = getResources().getIdentifier("run" + i, "drawable", getPackageName());
                Drawable drawable = getDrawable(id);
                if (null != drawable) {
                    animationDrawable.addFrame(drawable, 100);
                }
            }
            mTarget.setImageDrawable(animationDrawable);
            return animationDrawable;
        }
    }
}
