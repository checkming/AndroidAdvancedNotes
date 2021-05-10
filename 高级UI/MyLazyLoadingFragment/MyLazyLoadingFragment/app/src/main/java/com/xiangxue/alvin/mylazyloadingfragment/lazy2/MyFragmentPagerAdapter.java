package com.xiangxue.alvin.mylazyloadingfragment.lazy2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.mylazyloadingfragment
 * @fileName MyFragmentPagerAdapter
 * @date on 2019/6/10
 * @qq 2464061231
 **/
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "MyFragmentPagerAdapter";

    private List<Fragment> fragmentList;
    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> flist) {
        super(fm);
        fragmentList = flist;
    }
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
