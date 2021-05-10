package com.xiangxue.alvin.mylazyloadingfragment.lazy3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiangxue.alvin.mylazyloadingfragment.R;


public class Fragment5 extends LazyFragment2 {

    private Button mReplace;
    private Button mHideShow;

    private Fragment mCurrentFragment;

    private Fragment fragment51;
    private Fragment fragment52;


    public static Fragment newIntance() {
        Fragment5 fragment = new Fragment5();
        fragment.setFragmentDelegater(new FragmentDelegater(fragment));
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_5;
    }

    @Override
    protected void initView(View view) {
        mReplace = view.findViewById(R.id.btnReplace);
        mHideShow = view.findViewById(R.id.btnHideShow);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        mCurrentFragment = fragment51 = Fragment5_1.newIntance();
        fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment, Fragment5_1.class.getName());
        fragmentTransaction.show(mCurrentFragment);
        fragmentTransaction.commit();


        mReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                if (mCurrentFragment instanceof Fragment5_1) {
                    mCurrentFragment = fragment52 = Fragment5_2.newIntance();
                    fragmentTransaction.replace(R.id.frameLayout_5, mCurrentFragment, Fragment5_2.class.getName());
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                } else {
                    mCurrentFragment = fragment51 = Fragment5_1.newIntance();
                    fragmentTransaction.replace(R.id.frameLayout_5, mCurrentFragment, Fragment5_1.class.getName());
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                }
            }
        });
        mHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                if (mCurrentFragment instanceof Fragment5_1) {
                    Fragment tmp = getChildFragmentManager().findFragmentByTag(Fragment5_2.class.getName());
                    fragmentTransaction.hide(mCurrentFragment);
                    if (tmp == null) {
                        mCurrentFragment = fragment52 = Fragment5_2.newIntance();
                        fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment, Fragment5_2.class.getName());
                    } else {
                        mCurrentFragment = tmp;
                    }
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                } else {
                    Fragment tmp = getChildFragmentManager().findFragmentByTag(Fragment5_1.class.getName());
                    fragmentTransaction.hide(mCurrentFragment);
                    if (tmp == null) {
                        mCurrentFragment = fragment51 = Fragment5_1.newIntance();
                        fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment, Fragment5_1.class.getName());
                    } else {
                        mCurrentFragment = tmp;
                    }
                    fragmentTransaction.show(mCurrentFragment).commitNowAllowingStateLoss();
                }
            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
