package com.romeo.weatherappnew.JSON;

import com.romeo.weatherappnew.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class CurrentWeatherAnswer implements AbstractAnswer{
    private static final String URI = "https://api.openweathermap.org/data/2.5/weather?q=%s%s%s";
    private Main main;
    private Wind wind;

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public static URL formURL(String city) throws MalformedURLException {
        String uri = String.format(URI, city, "&appid=", BuildConfig.WEATHER_API_KEY);
        return new URL(uri);
    }
}
