package com.enjoy.gps.location;

/**
 * @author Lance
 * @date 2019/1/7
 */
public class Location {

    String address;
    double lat;
    double lng;

    public Location(String address, double lat, double lng) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
