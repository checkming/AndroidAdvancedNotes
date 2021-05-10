package com.xiangxue.webviewapp.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.arch.demo.core.baseservices.ILoginService;
import com.google.android.material.textfield.TextInputLayout;
import com.limpoxe.support.servicemanager.ServiceManager;
import com.xiangxue.common.router.AppRouter;
import com.xiangxue.webviewapp.R;


/**
 * Created by Akshay Raj on 6/16/2016.
 * akshay@snowcorp.org
 * www.snowcorp.org
 */
@Route(path = AppRouter.APP_LOGIN_ACTIVITY)
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    private Button btnLogin, btnLinkToRegister, btnForgotPass;
    private TextInputLayout inputEmail, inputPassword;
    private ProgressDialog pDialog;
    private static ILoginService mLoginService = (ILoginService) ServiceManager.getService(ILoginService.LOGIN_SERVICE_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.lTextEmail);
        inputPassword = findViewById(R.id.lTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);
        btnForgotPass = findViewById(R.id.btnForgotPassword);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Hide Keyboard
                mLoginService.refreshUserDetailInfo();
                finish();
            }
        });
    }
}
