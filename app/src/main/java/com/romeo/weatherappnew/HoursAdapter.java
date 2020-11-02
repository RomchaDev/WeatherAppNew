package com.romeo.weatherappnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.romeo.weatherappnew.JSON.forecast.HourlyForecastAnswer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HourViewHolder> {

    private final static int HOURS_AMOUNT = 24;
    private static int hoursCounter = 0;
    private static final int[] hoursIntegers = new int[HOURS_AMOUNT];
    private final int[] temperatures = new int[HOURS_AMOUNT];
    private static HoursAdapter instance;
    private static String temperatureUnit;
    private final List<HourViewHolder> hourHolders = new ArrayList<>();


    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int listItemId = R.layout.main_activity_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        hoursCounter++;

        HourViewHolder viewHolder = new HourViewHolder(inflater.inflate(listItemId, parent, false));

        viewHolder.hourView.setText(String.valueOf(hoursCounter));

        return viewHolder;
    }

    public HoursAdapter() {
        instance = this;
        temperatureUnit = MainActivity.getInstance().getString(R.string.temperatureUnit);
    }

    public static HoursAdapter getInstance() {
        return instance;
    }

    public void resetForecast(HourlyForecastAnswer ans) {
        for (int i = 0; i < HOURS_AMOUNT; i++) {
            temperatures[i] = (int) ans.get(i);
        }

        for (int i = 0; i < hourHolders.size(); i++) {
            String s = ans.get(i) + temperatureUnit;
            hourHolders.get(i).tempView.setText(s);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        String finalString;

        String inputString = String.valueOf(hoursIntegers[position]);

        if (inputString.length() == 1)
            finalString = '0' + inputString + ":00";
        else
            finalString = inputString + ":00";

        holder.bind(finalString, temperatures[position] + temperatureUnit);
    }

    @Override
    public int getItemCount() {
        return HOURS_AMOUNT;
    }

    class HourViewHolder extends RecyclerView.ViewHolder {

        private final TextView hourView;
        private final TextView tempView;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);

            hourView = itemView.findViewById(R.id.list_text_top);
            tempView = itemView.findViewById(R.id.list_text_bottom);

            String s = temperatures[hoursCounter] + temperatureUnit;

            tempView.setText(s);

            hourHolders.add(this);
        }

        private void bind(String newHour, String newTemp) {
            hourView.setText(newHour);
            tempView.setText(newTemp);
        }

    }

    public void resetCurrentHour() {
        for (int i = 0; i < HOURS_AMOUNT; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, i);

            hoursIntegers[i] = calendar.get(Calendar.HOUR_OF_DAY);
        }
    }


}
