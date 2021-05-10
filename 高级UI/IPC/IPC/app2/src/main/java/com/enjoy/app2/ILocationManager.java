package com.enjoy.app2;



import com.enjoy.gps.location.Location;
import com.enjoy.ipc2.ServiceId;


@ServiceId("LocationManager")
public interface ILocationManager {

     Location getLocation();
}

