package com.enjoy.gps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.enjoy.gps.location.ILocationManager;
import com.enjoy.gps.location.Location;
import com.enjoy.gps.location.LocationManager;
import com.enjoy.ipc2.IPC2;
import com.enjoy.ipc2.IPCService;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, GpsService.class));

        IPC2.connect(this, IPCService.IPCService0.class);

    }

    public void showLocation(View view) {
        //代理对象
        ILocationManager location = IPC2.getInstanceWithName(IPCService.IPCService0.class,
                ILocationManager.class,"getDefault");


        Toast.makeText(this, "当前位置:" + location.getLocation(), Toast.LENGTH_LONG).show();
    }
}
