package com.romeo.weatherappnew.JSON.forecast;

import com.romeo.weatherappnew.BuildConfig;

public class DailyForecastAnswer implements ForecastAnswer {
    public static String URI =
            "https://api.openweathermap.org/data/2.5/onecall?" +
                    "lat=%f&lon=%f&exclude=hourly,current,alerts,minutely&appid=" + BuildConfig.WEATHER_API_KEY;

    private Day[] daily;

    public Day get(int index) {
        return daily[index];
    }
}
