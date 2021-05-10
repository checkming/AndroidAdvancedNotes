package com.xiangxue.alvin.mylazyloadingfragment.lazy2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 享学课堂 Alvin
 * @package com.xiangxue.alvin.mylazyloadingfragment.lazy2
 * @fileName LazyFragment
 * @date on 2019/7/4
 * @qq 2464061231
 **/
public abstract class LazyFragment extends Fragment {
    private static final String TAG = "LazyFragment";
    //fragment 生命周期：
    // onAttach -> onCreate -> onCreatedView -> onActivityCreated -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy -> onDetach
    //对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有：
    //onCreatedView + onActivityCreated + onResume + onPause + onDestroyView

    protected View rootView = null;
    //view 是否已经创建
    boolean isViewCreated = false;
    //是否第一次创建的标志位
    boolean mIsFirstVisible = true;

    // 为了获得 Fragment 不可见的状态，和再次回到可见状态的判断，我们还需要增加一个 currentVisibleState 标志位，
    // 该标志位在 onResume 中和 onPause 中结合 getUserVisibleHint 的返回值来决定是否应该回调可见和不可见状态函数
    boolean currentVisibleState = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }
        //initView 用于添加默认的界面
        initView(rootView);
        // 将 View 创建完成标志位设为 true
        isViewCreated = true;
        Log.d(TAG, "onCreateView: ");
        // 本次分发主要时用于分发默认tab可见状态，这种状况下它的生命周期是：1 fragment setUserVisibleHint: true-》onAttach-》onCreate-》onCreateView-》onResume
        // 默认 Tab getUserVisibleHint() = true !isHidden() = true
        // 对于非默认 tab 或者非默认显示的 Fragment 在该生命周期中只做了 isViewCreated 标志位设置 可见状态将不会在这里分发
        if (!isHidden() && getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }
        return rootView;
    }

    protected abstract int getLayoutRes();

    protected abstract void initView(View view);


    //修改fragment的可见性
    //setUserVisibleHint 被调用有两种情况：
    // 1） 在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数，可以看log，
    //     对于默认 tab 和 间隔 checked tab 需要等到 isViewCreated = true 后才可以通过此通知用户可见
    //2）对于之前已经调用过setUserVisibleHint 方法的fragment后，让fragment从可见到不可见之间状态的变化
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: ");
        //  对于情况1）不予处理，用 isViewCreated 进行判断，如果isViewCreated false，说明它没有被创建
        if (isViewCreated) {
            //对于情况2）要分情况考虑：2.1）如果是不可见->可见是下面的情况 ；2.2）如果是可见->不可见是下面的情况
            //对于2.1）我们需要如何判断呢？首先必须是可见的（isVisibleToUser 为true）
            // 而且只有当可见状态进行改变的时候才需要切换（此时就添加了currentVisibleState来辅助判断），否则会出现反复调用的情况
            //从而导致事件分发带来的多次更新
            //对于2.2）如果是可见->不可见，判断条件恰好和 2.1）相反
            if (isVisibleToUser && !currentVisibleState ) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    /**
     * 统一处理用户可见信息分发
     * 分第一次可见，可见，不可见分发
     * @param isVisible
     */
    private void dispatchUserVisibleHint(boolean isVisible) {
        Log.d(TAG, "dispatchUserVisibleHint: ");
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
        } else {
            onFragmentPause();
        }
    }

    /**
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide
     * 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     * https://blog.csdn.net/u013278099/article/details/72869175
     *
     * 你会发现使用hide和show这时fragment的生命周期不再执行，
     * 不走任何的生命周期，
     * 这样在有的情况下，数据将无法通过生命周期方法进行刷新，
     * 所以你可以使用onHiddenChanged方法来解决这问题。
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged: ");
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    protected abstract void onFragmentFirstVisible();

    protected void onFragmentResume() {

    }

    protected void onFragmentPause() {

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
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
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        mIsFirstVisible = false;
    }

}
