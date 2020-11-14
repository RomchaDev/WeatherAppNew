package com.romeo.weatherappnew;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.romeo.weatherappnew.JSON.MainJSONWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class WeatherGetterService extends Service {
    private static URL url;

    private final HandlerThread handlerThread = new HandlerThread("WeatherGetterHandler");

    private Handler handler = null;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (handler == null && !handlerThread.isAlive()) {
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }

        handler.post(() -> {
            String json = getJSON();
            MainJSONWorker.getInstance().notifyAboutJSON(json);
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getJSON() {

        String readyString = null;

        HttpsURLConnection conn = null;

        try {
            conn = (HttpsURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()));

            readyString = reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return readyString;
    }

    public static void startWeatherGetter(Context context, URL url) {
        WeatherGetterService.url = url;
        context.startService(new Intent(context, WeatherGetterService.class));
    }
}
