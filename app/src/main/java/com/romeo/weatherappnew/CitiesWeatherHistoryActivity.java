package com.romeo.weatherappnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.romeo.weatherappnew.activities.MainActivity;
import com.romeo.weatherappnew.recycler_adapters.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class CitiesWeatherHistoryActivity extends AppCompatActivity {
    private static CitiesWeatherHistoryActivity instance;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_weather_history);

        instance = this;

        initList();
    }

    private void initList() {
        RecyclerView recyclerView = findViewById(R.id.history_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(decoration);
    }

    public static CitiesWeatherHistoryActivity getInstance() {
        return instance;
    }

    public void notifyAboutCity(String city) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.CITY_NAME, city);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);

        MenuItem clear = menu.findItem(R.id.clear_history);

        clear.setOnMenuItemClickListener(item -> {
            CityWeatherContainer.getInstance().clear();
            adapter.clear();
            return true;
        });

        return true;
    }
}