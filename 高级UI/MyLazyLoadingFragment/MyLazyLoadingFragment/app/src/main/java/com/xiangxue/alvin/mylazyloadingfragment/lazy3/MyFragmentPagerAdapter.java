/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiangxue.alvin.mylazyloadingfragment.lazy3;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


/**
 * FragmentPagerAdapter派生自PagerAdapter，它是用来呈现Fragment页面的，这些Fragment页面会一直保存在fragment manager中，以便用户可以随时取用。
 * 这个适配器最好用于有限个静态fragment页面的管理。尽管不可见的视图有时会被销毁，
 * 但用户所有访问过的fragment都会被保存在内存中。因此fragment实例会保存大量的各种状态，这就造成了很大的内存开销。所以如果要处理大量的页面切换，建议使用FragmentStatePagerAdapter.
 * 每一个使用FragmentPagerAdapter的ViewPager都要有一个有效的ID集合{@link #makeFragmentName(int, long)}，有效ID的集合就是Fragment的集合
 * 对于FragmentPagerAdapter的派生类，只需要重写getItem(int)和getCount()就可以了
 */
public abstract class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "MyFragmentPagerAdapter";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    @SuppressWarnings("ReferenceEquality")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        Log.i("Zero","position: " + position);
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment,
                    makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object
                + " v=" + ((Fragment) object).getView());
        mCurTransaction.detach((Fragment) object);
    }

    @SuppressWarnings("ReferenceEquality")
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }


    public long getItemId(int position) {
        return position;
    }

    private static String makeFragmentName(int viewId, long id) {
//        Log.i("Zero", "makeFragmentName viewId: " + viewId + ", id: " + id);
        return "android:switcher:" + viewId + ":" + id;
    }
}
