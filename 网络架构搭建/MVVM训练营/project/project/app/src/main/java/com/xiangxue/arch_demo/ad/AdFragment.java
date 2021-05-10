package com.xiangxue.arch_demo.ad;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.xiangxue.arch_demo.IMainActivity;
import com.xiangxue.arch_demo.R;
import com.xiangxue.arch_demo.databinding.FragmentAdBinding;

public class AdFragment extends Fragment {
    FragmentAdBinding mBinding;
    private Integer duration = 3;
    private static final int delayTime = 1000;   // 每隔1000 毫秒执行一次

    private Handler handler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (0 == duration) {
                dismissSplashView();
                return;
            } else {
                setDuration(--duration);
            }
            handler.postDelayed(timerRunnable, delayTime);
        }
    };

    private IMainActivity iMainActivity;

    public AdFragment(IMainActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ad, container, false);
        mBinding.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSplashView();
            }
        });

        setDuration(duration);
        handler.postDelayed(timerRunnable, delayTime);
        mBinding.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_img));
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return mBinding.getRoot();
    }

    private void setDuration(Integer duration) {
        this.duration = duration;
        mBinding.textview.setText(String.format("跳过\n%d s", duration));
    }

    private void dismissSplashView() {
        if (iMainActivity != null) {
            iMainActivity.removeMeAndGoNextFragment();
        }
        handler.removeCallbacks(timerRunnable);
    }
}
