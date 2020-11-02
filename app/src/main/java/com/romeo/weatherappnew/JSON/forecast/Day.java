package com.romeo.weatherappnew.JSON.forecast;

public class Day {
    private Temp temp;

    public long getTemp() {
        return Math.round(temp.getEve() - 273) ;
    }
}
