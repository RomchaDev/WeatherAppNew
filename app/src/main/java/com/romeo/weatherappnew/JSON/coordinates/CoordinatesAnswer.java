package com.romeo.weatherappnew.JSON.coordinates;

import com.romeo.weatherappnew.BuildConfig;
import com.romeo.weatherappnew.JSON.AbstractAnswer;

import java.net.MalformedURLException;
import java.net.URL;

public class CoordinatesAnswer implements AbstractAnswer {
    private Coordinates coord;
    private static final String URI = "https://api.openweathermap.org/data/2.5/weather?q=%s%s%s";

    public Coordinates getCoordinates() {
        return coord;
    }

    public static URL formURL(String city) throws MalformedURLException {
        String uri = String.format(URI, city, "&appid=", BuildConfig.WEATHER_API_KEY);
        return new URL(uri);
    }
}
