package com.romeo.weatherappnew.JSON.forecast;

import com.romeo.weatherappnew.BuildConfig;

public class HourlyForecastAnswer implements ForecastAnswer {
    public static String URI =
            "https://api.openweathermap.org/data/2.5/onecall?" +
                    "lat=%f&lon=%f&exclude=daily,current,alerts,minutely&appid=" + BuildConfig.WEATHER_API_KEY;


    private Hour[] hourly;

    public long get(int index) {
        return Math.round(hourly[index].getTemp() - 273);
    }
}
