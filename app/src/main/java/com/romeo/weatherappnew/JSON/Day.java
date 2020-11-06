package com.romeo.weatherappnew.JSON;

public class Day {
    private Temp temp;
    private Weather[] weather;

    public long getTemp() {
        return Math.round(temp.getEve() - 273) ;
    }

    public Weather getWeather() {
        return weather[0];
    }
}
