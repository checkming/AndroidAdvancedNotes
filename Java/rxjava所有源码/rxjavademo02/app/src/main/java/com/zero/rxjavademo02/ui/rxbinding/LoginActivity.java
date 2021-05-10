package com.zero.rxjavademo02.ui.rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zero.rxjavademo02.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.bt_login)
    Button mBtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        Observable<CharSequence> ObservableName = RxTextView.textChanges(mEtPhone);
        Observable<CharSequence> ObservablePassword = RxTextView.textChanges(mEtPassword);

        Observable.combineLatest(ObservableName, ObservablePassword
                , (phone, password) -> isPhoneValid(phone.toString()) && isPasswordValid(password.toString()))
                .subscribe(mBtLogin::setEnabled);

        RxView.clicks(mBtLogin)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show());


    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

}
