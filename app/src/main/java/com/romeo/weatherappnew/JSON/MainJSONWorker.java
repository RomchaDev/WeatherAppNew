package com.romeo.weatherappnew.JSON;

import com.google.gson.Gson;
import com.romeo.weatherappnew.BuildConfig;
import com.romeo.weatherappnew.JSON.coordinates.Coordinates;
import com.romeo.weatherappnew.JSON.coordinates.CoordinatesAnswer;
import com.romeo.weatherappnew.activities.MainActivity;

import java.io.IOException;
import java.io.InputStreamReader;
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

    public void getUniversalForecast(String city) {
        Thread thread = new Thread(() -> {
            HttpsURLConnection conn = null;

            try {
                Coordinates coordinates = getCoordinates(city);
                String coordinatesUri = String.format(UniversalForecastAnswer.URI, coordinates.getLat(), coordinates.getLon());
                URL url = new URL(coordinatesUri);

                conn = (HttpsURLConnection) url.openConnection();

                Gson gson = new Gson();
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());

                UniversalForecastAnswer answer = gson.fromJson(reader, UniversalForecastAnswer.class);

                MainActivity.getInstance().notifyAboutWeatherChanges(answer);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        });

        thread.setDaemon(true);

        thread.start();
    }
    
    /*
     "https://api.openweathermap.org/data/2.5/onecall?" +
             "lat=%d&lon=%d&exclude=daily,current,alerts,minutely&appid=%s"*/
}
