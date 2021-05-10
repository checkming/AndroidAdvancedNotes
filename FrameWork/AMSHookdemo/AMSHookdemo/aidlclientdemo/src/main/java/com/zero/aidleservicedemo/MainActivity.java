package com.zero.aidleservicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Zero";

    private static final String ACTION = "com.zero.aidleservicedemo.service.action_myaidldemo";

    IMyAidlInterface myAidlInterface;
    @BindView(R.id.textView)
    TextView textView;

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        Intent explicitIntent = new Intent(implicitIntent);

        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    private ServiceConnection mConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接成功之后，会传递一个远程的Binder对象过来
            //然后转化成本地接口对象
            Log.i(TAG, "onServiceConnected: " + service);
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myAidlInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = new Intent(ACTION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bindService(createExplicitFromImplicitIntent(MainActivity.this,intent), mConn, BIND_AUTO_CREATE);
    }

    @OnClick(R.id.btn_getdata)
    public void onBtnGetdataClicked() {
        if(myAidlInterface!=null){
            try {
                String str =myAidlInterface.getData();
                Log.i(TAG, "onBtnGetdataClicked: "+ str);
                textView.setText(str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_sendData)
    public void onBtnSendDataClicked() {
        try {
            int ret = myAidlInterface.sendData("Hello aidl");
            Log.i(TAG, "onBtnSendDataClicked: " + ret);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myAidlInterface != null) {
            unbindService(mConn);
        }
    }
}
