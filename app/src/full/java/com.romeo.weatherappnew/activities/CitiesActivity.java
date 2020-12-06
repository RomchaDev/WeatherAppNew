package com.romeo.weatherappnew.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.romeo.weatherappnew.CityWeatherContainer;
import com.romeo.weatherappnew.R;

import java.io.IOException;
import java.util.List;

public class CitiesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_new);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SearchView searchView = findViewById(R.id.search_for_city_text);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                city = query;

                Geocoder geocoder = new Geocoder(CitiesActivity.this);

                try {
                    List<Address> list = geocoder.getFromLocationName(city, 1);

                    LatLng coordinates = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());

                    updateMap(coordinates, city);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng currentCoordinates = MainActivity.getInstance().getCurrentCoordinates();
        updateMap(currentCoordinates, "Your current position");
    }

    private void updateMap(LatLng coordinates, String place) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(coordinates).title(place));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));
    }

    @Override
    public void onBackPressed() {
        CityWeatherContainer.getInstance().addCity(city);

        Intent resultIntent = new Intent();

        resultIntent.putExtra(MainActivity.CITY_NAME, city);

        setResult(RESULT_OK, resultIntent);

        finish();

        super.onBackPressed();
    }
}