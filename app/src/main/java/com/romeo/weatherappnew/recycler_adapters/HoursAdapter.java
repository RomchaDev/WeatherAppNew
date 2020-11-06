package com.romeo.weatherappnew.recycler_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.romeo.weatherappnew.JSON.UniversalForecastAnswer;
import com.romeo.weatherappnew.R;
import com.romeo.weatherappnew.activities.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HourViewHolder> {

    private final static int HOURS_AMOUNT = 24;
    private static int hoursCounter = 0;
    private static final int[] hoursIntegers = new int[HOURS_AMOUNT];
    private final int[] temperatures = new int[HOURS_AMOUNT];
    private final String[] imageLabels = new String[HOURS_AMOUNT];
    private static String temperatureUnit;
    private final Map<HourViewHolder, Integer> hourHoldersNew = new HashMap<>();


    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int listItemId = R.layout.main_activity_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        hoursCounter++;

        HourViewHolder viewHolder = new HourViewHolder(inflater.inflate(listItemId, parent, false));

        return viewHolder;
    }

    public HoursAdapter() {
        temperatureUnit = MainActivity.getInstance().getString(R.string.temperature_unit);
    }

    public void resetForecastUniversal(UniversalForecastAnswer universalAnswer) {
        for (int i = 0; i < HOURS_AMOUNT; i++) {
            temperatures[i] = universalAnswer.getHour(i).getTemp();
            imageLabels[i] = universalAnswer.getHour(i).getWeather().getIcon();
        }

        for (HourViewHolder holder : hourHoldersNew.keySet()) {
            String s = universalAnswer.getHour(hourHoldersNew.get(holder)).getTemp() + temperatureUnit;
            holder.tempView.setText(s);
            holder.setImage(universalAnswer.getHour(hourHoldersNew.get(holder)).getWeather().getIcon());
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

        hourHoldersNew.put(holder, position);

        holder.bind(finalString, temperatures[position] + temperatureUnit, imageLabels[position]);
    }

    @Override
    public int getItemCount() {
        return HOURS_AMOUNT;
    }

    class HourViewHolder extends RecyclerView.ViewHolder {

        private final int id;
        private final TextView hourView;
        private final TextView tempView;
        private final ImageView weatherImage;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);

            hourView = itemView.findViewById(R.id.list_text_top);
            tempView = itemView.findViewById(R.id.list_text_bottom);
            weatherImage = itemView.findViewById(R.id.main_list_image);

            String s = temperatures[hoursCounter] + temperatureUnit;

            tempView.setText(s);
            hourView.setText(String.valueOf(hoursCounter));
            Picasso.get().load(String.format(MainActivity.IMAGE_URI, imageLabels[hoursCounter])).into(weatherImage);

            hourHoldersNew.put(this, hoursCounter);

            id = hoursCounter;
        }

        private void bind(String newHour, String newTemp, String imageLabel) {
            hourView.setText(newHour);
            tempView.setText(newTemp);
            Picasso.get().load(String.format(MainActivity.IMAGE_URI, imageLabel)).into(weatherImage);
        }

        public int getId() {
            return id;
        }

        public void setImage(String label) {
            Picasso.get().load(String.format(MainActivity.IMAGE_URI, label)).into(weatherImage);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof HourViewHolder)
                return ((HourViewHolder) obj).getId() == id;
            else
                throw new RuntimeException("It's not HourHolder");
        }

        @Override
        public int hashCode() {
            return id;
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
