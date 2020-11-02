package com.romeo.weatherappnew.JSON;

import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import com.google.gson.Gson;
import com.romeo.weatherappnew.BuildConfig;
import com.romeo.weatherappnew.JSON.coordinates.Coordinates;
import com.romeo.weatherappnew.JSON.coordinates.CoordinatesAnswer;
import com.romeo.weatherappnew.JSON.forecast.DailyForecastAnswer;
import com.romeo.weatherappnew.JSON.forecast.ForecastAnswer;
import com.romeo.weatherappnew.JSON.forecast.HourlyForecastAnswer;
import com.romeo.weatherappnew.MainActivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainJSONWorker {
    private static final String CURRENT_WEATHER_STR = "https://api.openweathermap.org/data/2.5/weather?q=%s%s%s";

    private static final MainJSONWorker instance = new MainJSONWorker();

    private MainJSONWorker() {
    }

    public static MainJSONWorker getInstance() {
        return instance;
    }

    public void getCurrentWeather(String city) {

        String uri = String.format(CURRENT_WEATHER_STR, city, "&appid=", BuildConfig.WEATHER_API_KEY);

        new Thread(() -> {
            HttpsURLConnection conn = null;

            try {
                URL url = new URL(uri);

                conn = (HttpsURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);

                InputStreamReader reader = new InputStreamReader(conn.getInputStream());

                CurrentWeatherAnswer answer = new Gson().fromJson(reader, CurrentWeatherAnswer.class);

                reader.close();

                MainActivity.getInstance().notifyAboutWeatherChanges(answer);
            } catch (IOException e) {
                MainActivity.getInstance().runOnUiThread(() ->
                        Toast.makeText(MainActivity.getInstance(), "Sorry, something went wrong...", Toast.LENGTH_LONG).show());
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        }).start();


    }

    public void getDailyForecast(String city) {
        new Thread(() -> {
            HttpsURLConnection forecastConn = null;

            try {
                Coordinates coordinates = getCoordinates(city);

                String forecastUri = String.format(DailyForecastAnswer.URI, coordinates.getLat(), coordinates.getLon());

                URL forecastUrl = new URL(forecastUri);

                forecastConn = (HttpsURLConnection) forecastUrl.openConnection();

                InputStreamReader forecastReader = new InputStreamReader(forecastConn.getInputStream());

                DailyForecastAnswer forecastAnswer = new Gson().fromJson(forecastReader, DailyForecastAnswer.class);

                forecastReader.close();

                MainActivity.getInstance().notifyAboutWeatherChanges(forecastAnswer);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (forecastConn != null)
                    forecastConn.disconnect();
            }
        }).start();
    }

    public void getHourlyForecast(String city) {
        new Thread(() -> {
            HttpsURLConnection hourlyConn = null;

            try {
                Coordinates coordinates = getCoordinates(city);

                String forecastUri = String.format(HourlyForecastAnswer.URI, coordinates.getLat(), coordinates.getLon());

                URL forecastUrl = new URL(forecastUri);

                hourlyConn = (HttpsURLConnection) forecastUrl.openConnection();

                InputStreamReader forecastReader = new InputStreamReader(hourlyConn.getInputStream());

                HourlyForecastAnswer forecastAnswer = new Gson().fromJson(forecastReader, HourlyForecastAnswer.class);

                forecastReader.close();

                MainActivity.getInstance().notifyAboutWeatherChanges(forecastAnswer);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (hourlyConn != null)
                    hourlyConn.disconnect();
            }
        }).start();
    }
    
    private Coordinates getCoordinates(String city) {
        HttpsURLConnection coordinatesConn = null;

        try {

            String coordinatesUri = String.format(CURRENT_WEATHER_STR, city, "&appid=", BuildConfig.WEATHER_API_KEY);

            URL coordinatesUrl = new URL(coordinatesUri);

            coordinatesConn = (HttpsURLConnection) coordinatesUrl.openConnection();

            InputStreamReader coordinatesReader = new InputStreamReader(coordinatesConn.getInputStream());

            CoordinatesAnswer coordinatesAnswer = new Gson().fromJson(coordinatesReader, CoordinatesAnswer.class);

            coordinatesReader.close();

            return coordinatesAnswer.getCoordinates();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (coordinatesConn != null)
                coordinatesConn.disconnect();
        }

        return null;
    }
    
    /*
     "https://api.openweathermap.org/data/2.5/onecall?" +
             "lat=%d&lon=%d&exclude=daily,current,alerts,minutely&appid=%s"*/
}
