package com.zero.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class PropertyAnimationActivity extends AppCompatActivity {

    private View mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        mTarget = findViewById(R.id.view_target);
        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_property, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        } else if (i == R.id.action_do_byxml) {
            doAnimation(getAnimationDrawable(true));
        } else if (i == R.id.action_bycode) {
            doAnimation(getAnimationDrawable(false));
        } else if (i == R.id.action_bycustom) {
            doAnimation(getValueAnimatorByCustom());
        } else if (i == R.id.action_byviewpropertyanimator) {
            doAnimatorByViewPropertyAnimator();
        } else if (i == R.id.action_bylayoutanimator) {
            doLayoutAnimator();
        }
        return super.onOptionsItemSelected(item);
    }

    private Animator getAnimationDrawable(boolean formXml) {
        return formXml ? getAnimationByXml() : getAnimatorSet();
    }


    private void doAnimation(Animator animator) {
        animator.start();
        //animator.cancel();
        //animator.end();
        //animator.isPaused();
        //animator.isRunning();
        //animator.isStarted();
    }


    private Animator getAnimationByXml() {
        final int height = mTarget.getLayoutParams().height;
        final int width = mTarget.getLayoutParams().width;
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animatorset);

        ArrayList<Animator> childAnimations = animatorSet.getChildAnimations();
        ((ValueAnimator) childAnimations.get(childAnimations.size() - 1))
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedValue = (float) valueAnimator.getAnimatedValue();
                        mTarget.getLayoutParams().height = (int) (height * animatedValue);
                        mTarget.getLayoutParams().width = (int) (width * animatedValue);
                        mTarget.requestLayout();
                    }
                });

        animatorSet.setTarget(mTarget);
        return animatorSet;
    }


    public ObjectAnimator getObjectAnimator(boolean b) {
        if (b) {
            ObjectAnimator bgColorAnimator = ObjectAnimator.ofArgb(mTarget,
                    "backgroundColor",
                    0xff009688, 0xff795548);
            bgColorAnimator.setRepeatCount(1);
            bgColorAnimator.setDuration(3000);
            bgColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
            bgColorAnimator.setStartDelay(0);
            return bgColorAnimator;
        } else {
            ObjectAnimator rotationXAnimator = ObjectAnimator.ofFloat(mTarget,
                    "rotationX",
                    0f, 360f);
            rotationXAnimator.setRepeatCount(1);
            rotationXAnimator.setDuration(3000);
            rotationXAnimator.setRepeatMode(ValueAnimator.REVERSE);
            return rotationXAnimator;
        }
    }


    public Animator getObjectAnimatorByPropertyValuesHolder() {
        PropertyValuesHolder bgColorAnimator = PropertyValuesHolder.ofObject("backgroundColor",
                new ArgbEvaluator(),
                0xff009688, 0xff795548);
        PropertyValuesHolder rotationXAnimator = PropertyValuesHolder.ofFloat("rotationX",
                0f, 360f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mTarget, bgColorAnimator, rotationXAnimator);
        objectAnimator.setDuration(3000);
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        return objectAnimator;
    }


    public ValueAnimator getValueAnimator() {
        final int height = mTarget.getLayoutParams().height;
        final int width = mTarget.getLayoutParams().width;

        ValueAnimator sizeValueAnimator = ValueAnimator.ofFloat(1f, 3f);
        sizeValueAnimator.setDuration(3000);
        sizeValueAnimator.setRepeatCount(1);
        sizeValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        sizeValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                mTarget.getLayoutParams().height = (int) (height * animatedValue);
                mTarget.getLayoutParams().width = (int) (width * animatedValue);
                mTarget.requestLayout();
            }
        });
        return sizeValueAnimator;
    }

    public AnimatorSet getAnimatorSet() {
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(getObjectAnimatorByPropertyValuesHolder(), getValueAnimator());

        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

//-----------------------------高级用法----------------------------------

    static class PropertyBean {
        int backgroundColor;
        float rotationX;
        float rotationY;
        float rotationZ;
        float size;

        private float getRotationY() {
            return rotationY;
        }

        private void setRotationY(final float rotationY) {
            this.rotationY = rotationY;
        }

        public PropertyBean(int backgroundColor, float rotationX, float rotationY,float rotationZ, float size) {
            this.backgroundColor = backgroundColor;
            this.rotationX = rotationX;
            this.rotationY = rotationY;
            this.rotationZ = rotationZ;
            this.size = size;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public float getRotationX() {
            return rotationX;
        }

        private float getRotationZ() {
            return rotationZ;
        }

        private void setRotationZ(final float rotationZ) {
            this.rotationZ = rotationZ;
        }

        public void setRotationX(float rotationX) {
            this.rotationX = rotationX;
        }

        public float getSize() {
            return size;
        }

        public void setSize(float size) {
            this.size = size;
        }
    }

    static class SpeedUpInterpolator implements Interpolator {
        private final float mFactor;
        private final double mDoubleFactor;

        public SpeedUpInterpolator() {
            mFactor = 1.0f;
            mDoubleFactor = 2.0;
        }

        @Override
        public float getInterpolation(float v) {
            //实现核心代码块
            if (mFactor == 1.0f) {
                return v * v;
            } else {
                return (float) Math.pow(v, mDoubleFactor);
            }
        }
    }

    public class MyTypeEvaluator implements TypeEvaluator<PropertyBean> {
        ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

        @Override
        public PropertyBean evaluate(float fraction, PropertyBean startPropertyBean, PropertyBean endPropertyBean) {
            int currentColor = (int) mArgbEvaluator.evaluate(fraction, startPropertyBean.getBackgroundColor(), endPropertyBean.getBackgroundColor());
            float currentRotationX = startPropertyBean.getRotationX() + (endPropertyBean.getRotationX() - startPropertyBean.getRotationX()) * fraction;
            float currentRotationY = startPropertyBean.getRotationY() + (endPropertyBean.getRotationY() - startPropertyBean.getRotationY()) * fraction;
            float currentRotationZ = startPropertyBean.getRotationZ() + (endPropertyBean.getRotationZ() - startPropertyBean.getRotationZ()) * fraction;
            float currentSize = startPropertyBean.getSize() + (endPropertyBean.getSize() - startPropertyBean.getSize()) * fraction;
            return new PropertyBean(currentColor, currentRotationX,currentRotationY,currentRotationZ, currentSize);
        }
    }

    private Animator getValueAnimatorByCustom() {
        final int height = mTarget.getLayoutParams().height;
        final int width = mTarget.getLayoutParams().width;
        PropertyBean startPropertyBean = new PropertyBean(0xff009688, 0f,360, 0,1f);
        PropertyBean endPropertyBean = new PropertyBean(0xff795548, 360f,0, 360,3.0f);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new SpeedUpInterpolator());//custom interpolator
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(1);

        valueAnimator.setObjectValues(startPropertyBean, endPropertyBean);
        valueAnimator.setEvaluator(new MyTypeEvaluator());//custom evaluator

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PropertyBean propertyBean = (PropertyBean) valueAnimator.getAnimatedValue();
                if (propertyBean.getBackgroundColor() != 0 && propertyBean.getBackgroundColor() != 1) {
                    mTarget.setBackgroundColor(propertyBean.getBackgroundColor());
                }
                mTarget.setRotationX(propertyBean.getRotationX());
                mTarget.setRotationY(propertyBean.getRotationY());
                mTarget.setRotation(propertyBean.rotationZ);
                mTarget.getLayoutParams().height = (int) (height * propertyBean.getSize());
                mTarget.getLayoutParams().width = (int) (width * propertyBean.getSize());
                mTarget.requestLayout();
//                mTarget.postInvalidate();
            }
        });
        return valueAnimator;
    }

    private void doAnimatorByViewPropertyAnimator() {
        mTarget.animate()
                .rotationX(360f)
                .alpha(0.5f)
                .scaleX(3).scaleY(3)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(3000)
                .setStartDelay(0);

    }

    private void doLayoutAnimator() {
        LayoutTransition layoutTransition = new LayoutTransition();

        layoutTransition.setAnimator(LayoutTransition.APPEARING, getObjectAnimator(false));
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, getObjectAnimator(true));
        layoutTransition.setDuration(2000);

        ViewGroup contentView = (ViewGroup) ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
        contentView.setLayoutTransition(layoutTransition);
        if (contentView.findViewById(R.id.view_target) == null) {
            contentView.addView(mTarget);
        } else {
            contentView.removeView(mTarget);
        }
    }

}
