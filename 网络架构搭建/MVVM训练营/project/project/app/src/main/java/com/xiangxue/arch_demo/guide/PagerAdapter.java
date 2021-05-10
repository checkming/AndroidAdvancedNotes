package com.xiangxue.arch_demo.guide;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Integer> mGuideImageUrls = new ArrayList<>();

    public PagerAdapter(FragmentManager fm, ArrayList<Integer> urls) {
        super(fm);
        mGuideImageUrls.addAll(urls);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new FragmentGuide();
        Bundle bundle = new Bundle();
        bundle.putString(FragmentGuide.GUIDE_URL, String.valueOf(mGuideImageUrls.get(position)));
        bundle.putBoolean(FragmentGuide.IS_LAST_FRAGMENT, position == (mGuideImageUrls.size() -1));
        f.setArguments(bundle);
        return f;
    }

    @Override
    public int getCount() { // Return the number of pages
        return mGuideImageUrls.size();
    }
}