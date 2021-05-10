package com.zero.rxjavademo02.ui.rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zero.rxjavademo02.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class ButtonClicksActivity extends AppCompatActivity {

    private static String TAG = "ButtonClicksActivity";

    @BindView(R.id.bt)
    Button mBt;
    @BindView(R.id.bt1)
    Button mBt1;

    private CompositeDisposable mCompositeSubscription = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_click);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        try {
            RxTextView.text(mBt).accept("猛击按钮");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 按钮点击防抖
         */
        RxView.clicks(mBt)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> Log.i(TAG, "点击按钮"));



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.dispose();
        }
    }

}
