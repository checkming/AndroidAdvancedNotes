package com.xiangxue.alvin.mylazyloadingfragment.lazy3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class FragmentDelegater extends Fragment {

    Fragment mFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dumpLifeCycle("onAttach: " + mFragment.hashCode());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dumpLifeCycle("onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dumpLifeCycle("onCreateView");
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dumpLifeCycle("onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dumpLifeCycle("onActivityCreated");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        dumpLifeCycle("onViewStateRestored");
    }

    @Override
    public void onStart() {
        super.onStart();
        dumpLifeCycle("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        dumpLifeCycle("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        dumpLifeCycle("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        dumpLifeCycle("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dumpLifeCycle("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dumpLifeCycle("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dumpLifeCycle("onDetach");
    }

    @SuppressLint("ValidFragment")
    public FragmentDelegater(Fragment fragment) {
        super();
        this.mFragment = fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i("Alvin", mFragment.getClass().getSimpleName() + " -> setUserVisibleHint isVisibleToUser: " + isVisibleToUser + " =============");
    }

    /**
     * 第一次进来不会触发
     * 跳转到下一个页面的时候会触发：true
     * 在回来的时候会触发：false
     * 返回到上一级的时候 不会促发
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i("Zero", mFragment.getClass().getSimpleName() + " -> onHiddenChanged hidden: " + hidden + " ***************");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i("Zero", "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
//        dumpLifeCycle("onInflate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        dumpLifeCycle("onSaveInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        dumpLifeCycle("onConfigurationChanged");
    }

    public void dumpLifeCycle(final String method) {
        Log.i("Alvin", "name: " + mFragment.getClass().getSimpleName() + " -> " + method);
    }
}
