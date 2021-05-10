package com.xiangxue.alvin.mylazyloadingfragment.lazy3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.mylazyloadingfragment.lazy3
 * @fileName LazyFragment3
 * @date on 2019/7/7
 * @qq 2464061231
 **/
public abstract class LazyFragment3 extends Fragment {
    private static final String TAG = "LazyFragment3";
    //fragment 生命周期：
    // onAttach -> onCreate -> onCreatedView -> onActivityCreated -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy -> onDetach
    //对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有：
    //onCreatedView + onActivityCreated + onResume + onPause + onDestroyView

    protected View rootView = null;
    boolean isViewCreated = false;
    boolean currentVisibleState = false;
    boolean mIsFirstVisible = true;
    FragmentDelegater mFragmentDelegater;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }
        initView(rootView);
        isViewCreated = true;
        logD("onCreateView: ");
        //初始化的时候，判断当前fragment可见状态
        //todo, isHidden()什么时候调用？原理
        if (!isHidden() && getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }
        return rootView;
    }

    protected abstract int getLayoutRes();

    protected abstract void initView(View view);



    //修改fragment的可见性
    //setUserVisibleHint 被调用有两种情况：1） 在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数，可以看log
    //2）对于之前已经调用过setUserVisibleHint 方法的fragment后，让fragment从可见到不可见之间状态的变化
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        logD("setUserVisibleHint: " + isVisibleToUser);
        //  对于情况1）不予处理，用 isViewCreated 进行判断，如果isViewCreated false，说明它没有被创建
        if (isViewCreated) {
            //对于情况2）要分情况考虑，如果是不可见->可见是下面的情况 2.1），如果是可见->不可见是下面的情况2.2）
            //对于2.1）我们需要如何判断呢？首先必须是可见的（isVisibleToUser 为true）而且只有当可见状态进行改变的时候才需要切换，否则会出现反复调用的情况
            //从而导致事件分发带来的多次更新
            if (isVisibleToUser && !currentVisibleState ) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    /**
     * 统一处理用户可见信息分发
     * @param isVisible
     */
    private void dispatchUserVisibleHint(boolean isVisible) {
        logD( "dispatchUserVisibleHint: " + isVisible);
        //事实上作为父 Fragment 的 BottomTabFragment2 并没有分发可见事件，
        // 他通过 getUserVisibleHint() 得到的结果为 false，首先我想到的
        // 是能在负责分发事件的方法中判断一下当前父 fragment 是否可见，
        // 如果父 fragment 不可见我们就不进行可见事件的分发
        if (isVisible && isParentInvisible()) {
            return;
        }
        //为了代码严谨
        if (currentVisibleState == isVisible) {
            return;
        }
        currentVisibleState = isVisible;
        if (isVisible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            //在双重ViewPager嵌套的情况下，第一次滑到Frgment 嵌套ViewPager(fragment)的场景的时候
            //此时只会加载外层Fragment的数据，而不会加载内嵌viewPager中的fragment的数据，因此，我们
            //需要在此增加一个当外层Fragment可见的时候，分发可见事件给自己内嵌的所有Fragment显示
//            dispatchChildVisibleState(true);
        } else {
            onFragmentPause();
//            dispatchChildVisibleState(false);
        }
    }

    private boolean isParentInvisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof LazyFragment3) {
            LazyFragment3 fragment = (LazyFragment3)parentFragment;
            return !fragment.isSupportVisible();
        }
        return false;
    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }

    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment: fragments) {
                if (fragment instanceof LazyFragment3 &&
                        !fragment.isHidden() &&
                        fragment.getUserVisibleHint()) {
                    ((LazyFragment3)fragment).dispatchUserVisibleHint(visible);
                }
            }
        }
    }

    /**
     *
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide
     * 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     * https://blog.csdn.net/u013278099/article/details/72869175
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        logD("onHiddenChanged: " + hidden);
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    protected abstract void onFragmentFirstVisible();

    protected void onFragmentResume() {
        logD("onFragmentResume " + " 真正的resume,开始相关操作耗时");
    }

    protected void onFragmentPause() {
        logD("onFragmentPause" + " 真正的Pause,结束相关操作耗时");
    }

    public void setFragmentDelegater(FragmentDelegater fragmentDelegater) {
        mFragmentDelegater = fragmentDelegater;
    }

    @Override
    public void onResume() {
        super.onResume();
        logD( "onResume: ");
        //在滑动或者跳转的过程中，第一次创建fragment的时候均会调用onResume方法，类似于在tab1 滑到tab2，此时tab3会缓存，这个时候会调用tab3 fragment的
        //onResume，所以，此时是不需要去调用 dispatchUserVisibleHint(true)的，因而出现了下面的if
        if (!mIsFirstVisible) {
            //由于Activit1 中如果有多个fragment，然后从Activity1 跳转到Activity2，此时会有多个fragment会在activity1缓存，此时，如果再从activity2跳转回
            //activity1，这个时候会将所有的缓存的fragment进行onResume生命周期的重复，这个时候我们无需对所有缓存的fragnment 调用dispatchUserVisibleHint(true)
            //我们只需要对可见的fragment进行加载，因此就有下面的if
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint
     * currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
     *  子 fragment 走到这里的时候自身又会调用一遍
     */
    @Override
    public void onPause() {
        super.onPause();
        logD( "onPause: ");
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        logD("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logD("onDestroyView");
        isViewCreated = false;
        mIsFirstVisible = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void logD(String infor) {
        if (mFragmentDelegater != null) {
            mFragmentDelegater.dumpLifeCycle(infor);
        }
    }
}
