package com.romeo.weatherappnew.JSON;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.LongDef;

import com.google.gson.Gson;
import com.romeo.weatherappnew.BuildConfig;
import com.romeo.weatherappnew.JSON.coordinates.Coordinates;
import com.romeo.weatherappnew.JSON.coordinates.CoordinatesAnswer;
import com.romeo.weatherappnew.WeatherGetterService;
import com.romeo.weatherappnew.activities.MainActivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainJSONWorker {

    private boolean isNotifyRequired = false;

    private static final String CURRENT_WEATHER_STR = "https://api.openweathermap.org/data/2.5/weather?q=%s%s%s";

    private static final MainJSONWorker instance = new MainJSONWorker();

    private String json;

    private MainJSONWorker() {
    }

    public static MainJSONWorker getInstance() {
        return instance;
    }

    private Coordinates getCoordinates(String city) {
        HttpsURLConnection coordinatesConn = null;

        try {

            String coordinatesUri = String.format(CURRENT_WEATHER_STR, city, "&appid=", BuildConfig.WEATHER_API_KEY);

            URL coordinatesUrl = new URL(coordinatesUri);

            WeatherGetterService.startWeatherGetter(MainActivity.getInstance(), coordinatesUrl);

            isNotifyRequired = true;

            while (isNotifyRequired) ;

            CoordinatesAnswer coordinatesAnswer = new Gson().fromJson(json, CoordinatesAnswer.class);

            return coordinatesAnswer.getCoordinates();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (coordinatesConn != null)
                coordinatesConn.disconnect();
        }

        return null;
    }

    public void getUniversalForecast(String city) {
        Log.d("DONE", "getUniversalForecast: done");

        try {
            Coordinates coordinates = getCoordinates(city);
            String coordinatesUri = String.format(UniversalForecastAnswer.URI, coordinates.getLat(), coordinates.getLon());
            URL url = new URL(coordinatesUri);

            WeatherGetterService.startWeatherGetter(MainActivity.getInstance(), url);

            isNotifyRequired = true;

            while (isNotifyRequired) ;



            Gson gson = new Gson();

            UniversalForecastAnswer answer = gson.fromJson(json, UniversalForecastAnswer.class);

            MainActivity.getInstance().notifyAboutWeatherChanges(answer);
        } catch (IOException e) {
            showDialog("Server error", "Sorry, problems with server! What do you want to do now?", city);
        } catch (NullPointerException e) {
            showDialog("Connection error", "A problem with your internet connection! What do you want to do now?", city);
        }
    }

    private void showDialog(String title, String message, String city) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());

        MainActivity.getInstance().runOnUiThread(() ->
                builder.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Retry", (dialog, which) -> getUniversalForecast(city))
                        .setNegativeButton("Exit", ((dialog, which) -> System.exit(0)))
                        .setOnCancelListener(dialog -> getUniversalForecast(city))
                        .show());

    }

    public synchronized void notifyAboutJSON(String json) {
        this.json = json;

        isNotifyRequired = false;
    }
    
    /*
     "https://api.openweathermap.org/data/2.5/onecall?" +
             "lat=%d&lon=%d&exclude=daily,current,alerts,minutely&appid=%s"*/
}
