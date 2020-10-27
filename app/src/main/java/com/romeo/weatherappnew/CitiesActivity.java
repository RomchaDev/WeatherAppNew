package com.romeo.weatherappnew;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CitiesActivity extends AppCompatActivity {
    private static CitiesActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        setContentView(R.layout.activity_cities);

        RecyclerView citiesView = findViewById(R.id.citiesHolder);

        citiesView.setLayoutManager(new LinearLayoutManager(this));

        citiesView.setAdapter(new CitiesAdapter(this));
    }

    public void chooseCity(String city) {
        Intent resultIntent = new Intent();

        resultIntent.putExtra(MainActivity.CITY_NAME, city);

        setResult(RESULT_OK, resultIntent);

        finish();
    }

    public static CitiesActivity getInstance() {
        return instance;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }
}
