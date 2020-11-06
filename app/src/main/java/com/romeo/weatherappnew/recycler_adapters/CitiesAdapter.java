package com.romeo.weatherappnew.recycler_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.romeo.weatherappnew.R;
import com.romeo.weatherappnew.activities.CitiesActivity;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityHolder> {

    private static final int CITIES_AMOUNT = 10;
    private static int cityCounter = 0;
    private static String[] cities;

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        return new CityHolder(inflater.inflate(R.layout.city_item, parent, false));
    }

    public CitiesAdapter(Context mainContext) {
        cities = mainContext.getResources().getStringArray(R.array.cities);
        cityCounter = 0;
    }

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        holder.bind(cities[position]);
    }

    @Override
    public int getItemCount() {
        return CITIES_AMOUNT;
    }

    static class CityHolder extends RecyclerView.ViewHolder {
        private final Button cityButton;

        public CityHolder(@NonNull View itemView) {
            super(itemView);

            cityButton = itemView.findViewById(R.id.city_container_button);

            cityButton.setText(cities[cityCounter]);

            cityCounter++;

            cityButton.setOnClickListener(v -> CitiesActivity.getInstance().chooseCity((String) cityButton.getText()));

        }

        private void bind(String city) {
            cityButton.setText(city);
        }

    }
}
