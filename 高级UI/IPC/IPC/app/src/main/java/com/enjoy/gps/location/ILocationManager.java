package com.enjoy.gps.location;


import com.enjoy.ipc2.ServiceId;

@ServiceId("LocationManager")
public interface ILocationManager {

     Location getLocation();
}

