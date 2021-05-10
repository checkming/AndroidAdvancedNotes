package com.zero.animationdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewAnimationActivity extends AppCompatActivity {
    private static final String TAG = "View Animation";
    private View mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        initToolbar();
        mTarget = findViewById(R.id.view_target);
    }

    private void initToolbar() {
        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        } else if (i == R.id.action_alpha) {
            doAnimation(getAlphaAnimation(), getString(R.string.alpha_animation));
        } else if (i == R.id.action_rotate) {
            doAnimation(getRotateAnimation(), getString(R.string.rotate_animation));
        } else if (i == R.id.action_scale) {
            doAnimation(getScaleAnimation(), getString(R.string.scale_animation));
        } else if (i == R.id.action_translate) {
            doAnimation(getTranslateAnimation(), getString(R.string.translate_animation));
        } else if (i == R.id.action_set) {
            doAnimation(getAnimationSet(false), getString(R.string.animation_set));
        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    private void doAnimation(Animation animation,  final String animationType) {
        Animation oldAnimation = mTarget.getAnimation();
        if (oldAnimation != null) {
            if (oldAnimation.hasStarted() || (!oldAnimation.hasEnded())) {
                oldAnimation.cancel();
                mTarget.clearAnimation();
            }
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, animationType + " start;");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, animationType + " end;");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, animationType + " repeat;");
            }
        });
        mTarget.startAnimation(animation);
    }

    private Animation getAnimationSet(boolean fromXML) {
        if (fromXML) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_animation);
            return animation;
        } else {
            AnimationSet innerAnimationSet = new AnimationSet(true);
            innerAnimationSet.setInterpolator(new BounceInterpolator());
            innerAnimationSet.setStartOffset(1000);
            innerAnimationSet.addAnimation(getScaleAnimation());
            innerAnimationSet.addAnimation(getTranslateAnimation());

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setInterpolator(new LinearInterpolator());

            animationSet.addAnimation(getAlphaAnimation());
            animationSet.addAnimation(getRotateAnimation());
            animationSet.addAnimation(innerAnimationSet);

            return animationSet;
        }
    }

    private Animation getAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setFillBefore(false);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        return alphaAnimation;
    }

    private Animation getRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
                getWidth() / 2, getHeight() / 2);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(1);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setFillBefore(false);
        rotateAnimation.setRepeatMode(Animation.REVERSE);
        return rotateAnimation;
    }

    private Animation getScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f,
                1f, 2f,
                getWidth() / 2, getHeight() / 2);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setRepeatCount(2);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillBefore(false);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        return scaleAnimation;
    }

    private Animation getTranslateAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, getWidth() * 2,
                0, getHeight() * 2);
        translateAnimation.setDuration(2000);
        translateAnimation.setRepeatCount(2);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillBefore(false);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        return translateAnimation;
    }

    private int getWidth() {
        return mTarget.getWidth();
    }

    private int getHeight() {
        return mTarget.getHeight();
    }
}
