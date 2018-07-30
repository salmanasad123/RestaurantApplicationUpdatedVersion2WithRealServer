package com.example.salman.restaurantapplication;

/**
 * Created by Salman on 7/30/2018.
 */

public class LocationEvent {

    double longitude;
    double latitude;

    public LocationEvent(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
