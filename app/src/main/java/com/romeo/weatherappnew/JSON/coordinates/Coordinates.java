package com.romeo.weatherappnew.JSON.coordinates;

public class Coordinates {
    private double lat;
    private double lon;

    public Coordinates() {
    }

    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
