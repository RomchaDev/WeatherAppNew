package com.romeo.weatherappnew.JSON;

public class Current {
    private float wind_speed;
    private float temp;
    private Weather[] weather;

    public Weather getWeather() {
        return weather[0];
    }

    public float getWindSpeed() {
        return wind_speed;
    }

    public float getTemp() {
        return temp;
    }
}
