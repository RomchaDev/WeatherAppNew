package com.romeo.weatherappnew;

import com.romeo.weatherappnew.JSON.UniversalForecastAnswer;
import com.romeo.weatherappnew.JSON.coordinates.CoordinatesAnswer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.QueryMap;
import retrofit2.http.GET;

public interface LinkMaker {

    @GET("data/2.5/onecall")
    Call<UniversalForecastAnswer> getUniversalAnswer(@QueryMap Map<String, String> args);

    @GET("data/2.5/weather")
    Call<CoordinatesAnswer> getCoordinatesAnswer(@QueryMap Map<String, String> args);

}
