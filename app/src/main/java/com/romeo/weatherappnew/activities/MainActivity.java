package com.romeo.weatherappnew.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.romeo.weatherappnew.CitiesWeatherHistoryActivity;
import com.romeo.weatherappnew.recycler_adapters.DaysAdapter;
import com.romeo.weatherappnew.recycler_adapters.HoursAdapter;
import com.romeo.weatherappnew.JSON.MainJSONWorker;
import com.romeo.weatherappnew.JSON.UniversalForecastAnswer;
import com.romeo.weatherappnew.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String CITY_NAME = "CITY_NAME";

    private static MainActivity instance;
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

        Thread timeChanger = new Thread(this::changeTime);
        timeChanger.setDaemon(true);
        timeChanger.start();
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
        cityButton.setOnClickListener(v -> startActivityForResult(new Intent(this, CitiesActivity.class), CITY_REQUEST));

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
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        startActivityForResult(new Intent(MainActivity.this, CitiesWeatherHistoryActivity.class),
                                CITY_REQUEST);
                    }
                })
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
            MainJSONWorker.getInstance().getUniversalForecast(newCity);
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
}