package com.zero.rxjavademo02.ui.rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zero.rxjavademo02.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = "RegisterActivity";

    @BindView(R.id.bt)
    Button mBt;

    private Observable<Boolean> verifyCodeObservable;

    private static int SECOND = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        verifyCodeObservable = RxView.clicks(mBt)
                .throttleFirst(SECOND, TimeUnit.SECONDS) //防止20秒内连续点击,或者只使用doOnNext部分
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(o -> false)
                .doOnNext(mBt::setEnabled);
        verifyCodeObservable.subscribe(s -> {
            Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .take(SECOND)
                    .subscribe(aLong -> {
                                RxTextView.text(mBt).accept("剩余" + (SECOND - aLong) + "秒");
                            }
                            , Throwable::printStackTrace
                            , () -> {
                                RxTextView.text(mBt).accept("获取验证码");
                                RxView.enabled(mBt).accept(true);
                            });
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        verifyCodeObservable.unsubscribeOn(AndroidSchedulers.mainThread()); //防止泄露
    }

}
