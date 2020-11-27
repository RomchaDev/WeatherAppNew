package com.romeo.weatherappnew.JSON;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.HandlerThread;

import com.romeo.weatherappnew.BuildConfig;
import com.romeo.weatherappnew.JSON.coordinates.Coordinates;
import com.romeo.weatherappnew.JSON.coordinates.CoordinatesAnswer;
import com.romeo.weatherappnew.LinkMaker;
import com.romeo.weatherappnew.activities.MainActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainJSONWorker {

    private final Handler coordinatesHandler;

    private final Object monitor = new Object();

    private static final String BASE_URL = "https://api.openweathermap.org/";

    private Coordinates coordinates;

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final LinkMaker linkMaker = retrofit.create(LinkMaker.class);

    private static final MainJSONWorker instance = new MainJSONWorker();

    private MainJSONWorker() {
        HandlerThread coordinatesHandlerThread = new HandlerThread("coordinatesHandlerThread");
        coordinatesHandlerThread.start();
        coordinatesHandler = new Handler(coordinatesHandlerThread.getLooper());
    }

    public static MainJSONWorker getInstance() {
        return instance;
    }

    private void getCoordinates(String city) throws IOException {
        Map<String, String> args = new HashMap<>();

        args.put("q", city);

        args.put("appid", BuildConfig.WEATHER_API_KEY);

        Response<CoordinatesAnswer> response = linkMaker.getCoordinatesAnswer(args).execute();

        setCoordinates(response.body().getCoordinates());
    }

    public void getUniversalForecast(double lat, double lon) {
        Map<String, String> map = new HashMap<>();
        map.put("lat", String.valueOf(lat));
        map.put("lon", String.valueOf(lon));
        map.put("appid", BuildConfig.WEATHER_API_KEY);

        linkMaker.getUniversalAnswer(map).enqueue(new Callback<UniversalForecastAnswer>() {
            @Override
            public void onResponse(Call<UniversalForecastAnswer> call, Response<UniversalForecastAnswer> response) {
                MainActivity.getInstance().notifyAboutWeatherChanges(response.body());
            }

            @Override
            public void onFailure(Call<UniversalForecastAnswer> call, Throwable t) {
                showDialog("Network problems", "check your internet connection", null);
            }
        });
    }

    private void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void getUniversalForecast(String city) {

        coordinatesHandler.post(() -> {
            try {
                getCoordinates(city);

                synchronized (monitor) {
                    monitor.notify();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (coordinates == null) {
            showDialog("Network problems", "check your internet connection", city);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("lat", String.valueOf(coordinates.getLat()));
        map.put("lon", String.valueOf(coordinates.getLon()));
        map.put("appid", BuildConfig.WEATHER_API_KEY);

        linkMaker.getUniversalAnswer(map).enqueue(new Callback<UniversalForecastAnswer>() {
            @Override
            public void onResponse(Call<UniversalForecastAnswer> call, Response<UniversalForecastAnswer> response) {
                MainActivity.getInstance().notifyAboutWeatherChanges(response.body());
            }

            @Override
            public void onFailure(Call<UniversalForecastAnswer> call, Throwable t) {
                showDialog("Network problems", "check your internet connection", city);
            }
        });
    }

    private void showDialog(String title, String message, String city) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());

        MainActivity.getInstance().runOnUiThread(() ->
                builder.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Retry", (dialog, which) -> {
                            if (city != null)
                                getUniversalForecast(city);
                        })
                        .setNegativeButton("Exit", ((dialog, which) -> System.exit(0)))
                        .setOnCancelListener(dialog -> getUniversalForecast(city))
                        .show());
    }
}
