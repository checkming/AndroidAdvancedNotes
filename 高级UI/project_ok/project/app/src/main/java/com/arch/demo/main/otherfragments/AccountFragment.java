package com.arch.demo.main.otherfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.arch.demo.R;
import com.arch.demo.databinding.FragmentAccountBinding;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;


/**
 * Created by Vishal Patolia on 18-Feb-18.
 */
public class AccountFragment extends Fragment {
    FragmentAccountBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CCResult result = CC.obtainBuilder("UserCenter")  .setActionName("login")
                        .build().call();

            }
        });
        return mBinding.getRoot();
    }
}
