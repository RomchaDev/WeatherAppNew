package com.romeo.weatherappnew.recycler_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.romeo.weatherappnew.CitiesWeatherHistoryActivity;
import com.romeo.weatherappnew.CityWeatherContainer;
import com.romeo.weatherappnew.R;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private int citiesCounter = 0;

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new HistoryViewHolder(inflater.inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind((String) CityWeatherContainer.getInstance().getCitiesArray()[position]);
    }

    @Override
    public int getItemCount() {
        return CityWeatherContainer.getInstance().getCitiesArray().length;
    }

    public void clear() {
        notifyItemRangeRemoved(0, citiesCounter);
        citiesCounter = 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final Button button;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.history_item);

            Object[] names = CityWeatherContainer.getInstance().getCitiesArray();

            if (citiesCounter < names.length) {
                button.setText((String) names[citiesCounter]);
            }

            button.setOnClickListener(v ->
                    CitiesWeatherHistoryActivity.getInstance().notifyAboutCity((String) button.getText()));

            citiesCounter++;
        }

        private void bind(String city) {
            button.setText(city);
        }
    }
}
