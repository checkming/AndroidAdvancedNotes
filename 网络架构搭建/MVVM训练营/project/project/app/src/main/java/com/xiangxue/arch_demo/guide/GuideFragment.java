package com.xiangxue.arch_demo.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.xiangxue.arch_demo.IMainActivity;
import com.xiangxue.arch_demo.R;
import com.xiangxue.arch_demo.databinding.FragmentGuideHomeBinding;
import com.xiangxue.network.rxbus.RxBus;
import com.xiangxue.network.rxbus.Subscribe;

import java.util.ArrayList;

public class GuideFragment extends Fragment {
    private FragmentGuideHomeBinding mBinding;
    ArrayList<Integer> urls = new ArrayList<>();
    private IMainActivity iMainActivity;

    public GuideFragment(IMainActivity iMainActivity){
        this.iMainActivity = iMainActivity;
        urls.add(R.drawable.guide_test1);
        urls.add(R.drawable.guide_test2);
        urls.add(R.drawable.guide_test3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_home, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding.guideViewpager.setAdapter(new PagerAdapter(getChildFragmentManager(), urls));
        RxBus.getDefault().register(this);
        return mBinding.getRoot();
    }

    @Subscribe
    public void onStartMainAppEvent(StartMainAppEvent event) {
        if(iMainActivity != null) {
            iMainActivity.removeMeAndGoNextFragment();
        }
    }
}
