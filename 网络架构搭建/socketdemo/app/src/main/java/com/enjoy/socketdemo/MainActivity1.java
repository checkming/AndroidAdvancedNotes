package com.enjoy.socketdemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.enjoy.socketdemo.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity1 extends AppCompatActivity {
    private static final String TAG = "Zero";

    @BindView(R.id.tv_ip)
    TextView tvIp;
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.et_send)
    EditText etSend;

    private Intent mServiceIntent;

    private ISocket iSocket;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iSocket = null;

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iSocket = ISocket.Stub.asInterface(service);
        }
    };

    class MessageBackReciver extends BroadcastReceiver {
        private WeakReference<TextView> textView;

        public MessageBackReciver(TextView tv) {
            textView = new WeakReference<TextView>(tv);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TextView tv = textView.get();
            if (action.equals(SocketService.HEART_BEAT_ACTION)) {
                if (null != tv) {
                    Log.i(TAG, "Get a heart heat");
                    tv.setText("Get a heart heat");
                }
            } else {
                Log.i(TAG, "Get a heart heat");
                String message = intent.getStringExtra("message");
                tv.setText("服务器消息:" + message);
            }
        }

        ;
    }

    private MessageBackReciver mReciver;

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReciver = new MessageBackReciver(tvShow);

        mServiceIntent = new Intent(this, SocketService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SocketService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(SocketService.MESSAGE_ACTION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
        bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(conn);
        mLocalBroadcastManager.unregisterReceiver(mReciver);
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        String content = etSend.getText().toString();
        try {
            boolean isSend = iSocket.sendMessage(content);//Send Content by socket
            Toast.makeText(this, isSend ? "success" : "fail",
                    Toast.LENGTH_SHORT).show();
            etSend.setText("");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}