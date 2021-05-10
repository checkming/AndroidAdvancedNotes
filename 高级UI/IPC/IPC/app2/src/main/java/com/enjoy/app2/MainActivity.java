package com.enjoy.app2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.enjoy.ipc2.IPC2;
import com.enjoy.ipc2.IPCService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IPC2.connect(this,"com.enjoy.gps", IPCService.IPCService0.class);
    }


    public void test(View view) {
        //代理对象
        ILocationManager location = IPC2.getInstanceWithName(IPCService.IPCService0.class,
                ILocationManager.class,"getDefault");


        Toast.makeText(this, "当前位置:" + location.getLocation(), Toast.LENGTH_LONG).show();
    }
}
