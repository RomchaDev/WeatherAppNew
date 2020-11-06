package com.romeo.weatherappnew.JSON;

import com.romeo.weatherappnew.BuildConfig;

public class UniversalForecastAnswer implements AbstractAnswer{
    private Day[] daily;
    private Hour[] hourly;
    private Current current;

    public static final String URI = "https://api.openweathermap.org/data/2.5/onecall?" +
            "lat=%f&lon=%f&exclude=alerts,minutely&appid="
            + BuildConfig.WEATHER_API_KEY;

    public int getCurrentTemp() {
        return Math.round(current.getTemp() - 273);
    }

    public float getCurrentWindSpeed() {
        return current.getWindSpeed();
    }

    public Hour getHour(int index) {
        return hourly[index];
    }

    public Day getDay(int index) {
        return daily[index];
    }

    public Weather getCurrentWeather() {
        return current.getWeather();
    }
}
