package com.romeo.weatherappnew.JSON;

public class Hour {
    private float temp;
    private Weather[] weather;

    public int getTemp() {
        // return Math.round(temp - 273);
        return (int) (temp - 273);
    }

    public Weather getWeather() {
        return weather[0];
    }
}
