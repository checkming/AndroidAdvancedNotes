package com.xiangxue.news;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.xiangxue.base.activity.MvvmActivity;
import com.xiangxue.base.viewmodel.MvvmBaseViewModel;
import com.xiangxue.news.databinding.ActivityMainBinding;
import com.xiangxue.news.otherfragments.AccountFragment;
import com.xiangxue.news.otherfragments.CategoryFragment;
import com.xiangxue.news.otherfragments.ServiceFragment;

import java.lang.reflect.Field;

import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends MvvmActivity<ActivityMainBinding, MvvmBaseViewModel> {
    private Fragment mHomeFragment;
    private CategoryFragment mCategoryFragment = new CategoryFragment();
    private ServiceFragment mServiceFragment = new ServiceFragment();
    private AccountFragment mAccountFragment = new AccountFragment();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set Toolbar
        CCResult result = CC.obtainBuilder("News").setActionName("getHeadlineNewsFragment")
                .build().call();
        mHomeFragment = (Fragment)result.getDataMap().get("fragment");
        fromFragment = mHomeFragment;

        setSupportActionBar(viewDataBinding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.menu_home));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            disableShiftMode(viewDataBinding.bottomView);
        }
        viewDataBinding.bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        fragment = mHomeFragment;
                        break;
                    case R.id.menu_categories:
                        fragment = mCategoryFragment;
                        break;
                    case R.id.menu_services:
                        fragment = mServiceFragment;
                        break;
                    case R.id.menu_account:
                        fragment = mAccountFragment;
                        break;
                }
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(menuItem.getTitle());
                }
                switchFragment(fromFragment, fragment);
                fromFragment = fragment;
                return true;
            }
        });
        viewDataBinding.bottomView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewDataBinding.container.getId(), mHomeFragment);
        transaction.commit();
        showBadgeView(3, 5);

    }

    Fragment fromFragment;

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = manger.beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.add(R.id.container, to).commit();
                }

            } else {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.show(to).commit();
                }

            }
        }
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * BottomNavigationView????????????
     *
     * @param viewIndex  tab??????
     * @param showNumber ??????????????????????????????0???????????????
     */
    private void showBadgeView(int viewIndex, int showNumber) {
        // ??????child????????????view????????????????????????????????????
        // ???bottomNavigationView?????????BottomNavigationMenuView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) viewDataBinding.bottomView.getChildAt(0);
        // ???BottomNavigationMenuView?????????childview, BottomNavigationItemView
        if (viewIndex < menuView.getChildCount()) {
            // ??????viewIndex?????????tab
            View view = menuView.getChildAt(viewIndex);
            // ??????tab??????????????????????????????ImageView
            View icon = view.findViewById(com.google.android.material.R.id.icon);
            // ?????????????????????
            int iconWidth = icon.getWidth();
            // ??????tab?????????/2
            int tabWidth = view.getWidth() / 2;
            // ??????badge????????????????????????
            int spaceWidth = tabWidth - iconWidth;

            // ??????badegeview
            new QBadgeView(this).bindTarget(view).setGravityOffset(spaceWidth + 50, 13, false).setBadgeNumber(showNumber);
        }
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                // item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }
}
