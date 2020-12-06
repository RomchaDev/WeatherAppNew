package com.romeo.weatherappnew.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.romeo.weatherappnew.CitiesWeatherHistoryActivity;
import com.romeo.weatherappnew.JSON.CitiesActivityNew;
import com.romeo.weatherappnew.JSON.coordinates.Coordinates;
import com.romeo.weatherappnew.recycler_adapters.DaysAdapter;
import com.romeo.weatherappnew.recycler_adapters.HoursAdapter;
import com.romeo.weatherappnew.JSON.MainJSONWorker;
import com.romeo.weatherappnew.JSON.UniversalForecastAnswer;
import com.romeo.weatherappnew.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    public static final String CITY_NAME = "CITY_NAME";
    private final String DEBUG_TAG = "DEBUG_TAG";
    public static boolean turnedAutoGeolocationOn = true;
    private Criteria criteria;
    private LatLng currentCoordinates;
    private static MainActivity instance;
    public static final int GEO_PERMISSION_REQUEST_CODE = 5;
    private RecyclerView hoursList;
    private RecyclerView daysList;
    private DaysAdapter daysAdapter;
    private HoursAdapter hoursAdapter;
    private Button cityButton;
    private Button knowMoreButton;
    private TextView mainTime;
    private TextView mainDate;
    private static final int CITY_REQUEST = 101;
    private TextView mainTemp;
    private TextView mainWindSpeed;
    private TextView mainDescription;
    private ImageView mainWeatherIcon;
    public static final String IMAGE_URI = "https://openweathermap.org/img/wn/%s@4x.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        instance = this;

        findViews();
        initLists();
        setActionListeners();

        cityButton.setText(getResources().getStringArray(R.array.cities)[1]);

        replaceCityOnKnowMoreButtonTo((String) cityButton.getText());

        requestGeoPermissions();

        Thread timeChanger = new Thread(this::changeTime);
        timeChanger.setDaemon(true);
        timeChanger.start();
    }

    private void requestGeoPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            requestLocation();
        else {
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};

            requestPermissions(permissions, GEO_PERMISSION_REQUEST_CODE);
        }

    }

    private void requestLocation() {
        Log.d(DEBUG_TAG, "requestLocation: requesting");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Log.d(DEBUG_TAG, locationManager.getBestProvider(criteria, true));
        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true), 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d(DEBUG_TAG, "requestLocation: location changed");
                if (turnedAutoGeolocationOn) {
                    currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    MainJSONWorker.getInstance().getUniversalForecast(location.getLatitude(), location.getLongitude());
                } else {
                    currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }


        });
    }

    private void changeTime() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        hoursAdapter.resetCurrentHour();
        daysAdapter.resetCurrentDay();

        MainJSONWorker.getInstance().getUniversalForecast((String) cityButton.getText());

        while (true) {
            Calendar calendar = Calendar.getInstance();

            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));

            if (hour.length() == 1)
                hour = "0" + hour;

            if (minute.length() == 1)
                minute = "0" + minute;

            String date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
            String time = hour + ':' + minute;

            if (calendar.get(Calendar.HOUR_OF_DAY) != currentHour) {
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                hoursAdapter.resetCurrentHour();
                String city = (String) cityButton.getText();
                MainJSONWorker.getInstance().getUniversalForecast(city);
            }

            if (calendar.get(Calendar.DAY_OF_WEEK) != currentDay) {
                currentHour = calendar.get(Calendar.DAY_OF_WEEK);
                daysAdapter.resetCurrentDay();
            }

            runOnUiThread(() -> {
                mainTime.setText(time);
                mainDate.setText(date);
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void findViews() {
        daysList = findViewById(R.id.days_list);
        hoursList = findViewById(R.id.hours_list);
        cityButton = findViewById(R.id.city_button);
        knowMoreButton = findViewById(R.id.know_more_button);
        mainDate = findViewById(R.id.main_date);
        mainTime = findViewById(R.id.main_time);
        mainTemp = findViewById(R.id.mainTemp);
        mainWindSpeed = findViewById(R.id.mainWindSpeed);
        mainDescription = findViewById(R.id.mainDescription);
        mainWeatherIcon = findViewById(R.id.mainWeatherIcon);
    }

    private void setActionListeners() {
        cityButton.setOnClickListener(v -> startActivityForResult(new Intent(this, CitiesActivityNew.class), CITY_REQUEST));

        knowMoreButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://en.wikipedia.org/wiki/" + cityButton.getText());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    private void initLists() {
        initHoursList();

        initDaysList();

        Drawer.Result drawerResult = new Drawer()
                .withActivity(this)
                .addDrawerItems(formDrawerItems())
                .withAccountHeader(new AccountHeader().withActivity(this).withHeaderBackground(R.drawable.main_background).build())
                .withOnDrawerItemClickListener((parent, view, position, id, drawerItem) -> startActivityForResult(new Intent(MainActivity.this, CitiesWeatherHistoryActivity.class),
                        CITY_REQUEST))
                .build();
    }

    private PrimaryDrawerItem formDrawerItems() {
        return new PrimaryDrawerItem()
                .withIcon(R.drawable.weather_sunny)
                .withDescription("Recent cities...");
    }

    private void initDaysList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        daysList.setLayoutManager(layoutManager);
        daysAdapter = new DaysAdapter();
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        daysList.addItemDecoration(decoration);
        daysList.setAdapter(daysAdapter);
    }

    private void initHoursList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hoursList.setLayoutManager(layoutManager);
        hoursList.setHasFixedSize(true);
        hoursAdapter = new HoursAdapter();
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        hoursList.addItemDecoration(decoration);
        hoursList.setAdapter(hoursAdapter);
    }

    private void replaceCityOnKnowMoreButtonTo(String newCity) {
        String defaultStr = getResources().getString(R.string.know_more);
        knowMoreButton.setText(defaultStr.replace("city", newCity));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CITY_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String newCity = data.getStringExtra(CITY_NAME);
            cityButton.setText(newCity);
            replaceCityOnKnowMoreButtonTo(newCity);
            if (newCity != null)
                MainJSONWorker.getInstance().getUniversalForecast(newCity);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GEO_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                requestLocation();
            else requestGeoPermissions();
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void notifyAboutWeatherChanges(UniversalForecastAnswer ans) {
        changeWeather(ans);
    }

    private void changeWeather(UniversalForecastAnswer ans) {
        String temp = ans.getCurrentTemp() + getString(R.string.temperature_unit);
        String wind = ans.getCurrentWindSpeed() + " " + getString(R.string.wind_speed_unit);

        runOnUiThread(() -> {
            mainWindSpeed.setText(wind);
            mainTemp.setText(temp);
            mainDescription.setText(ans.getCurrentWeather().getDescription());
            Picasso.get().load(String.format(IMAGE_URI, ans.getCurrentWeather().getIcon())).into(mainWeatherIcon);

            daysAdapter.resetForecastUniversal(ans);
            hoursAdapter.resetForecastUniversal(ans);
        });
    }

    public LatLng getCurrentCoordinates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestGeoPermissions();
            return getCurrentCoordinates();
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());

        return currentCoordinates;
    }
}