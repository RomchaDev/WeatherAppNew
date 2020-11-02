package com.romeo.weatherappnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.romeo.weatherappnew.JSON.forecast.DailyForecastAnswer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayHolder> {

    private static final int DAYS_AMOUNT = 7;
    private static int daysCounter = 0;
    private static DaysAdapter instance;
    private static final List<DayHolder> dayHolders = new ArrayList<>();
    private final int[] daysIntegers = new int[DAYS_AMOUNT];
    private final int[] temperatures = new int[DAYS_AMOUNT];


    public static DaysAdapter getInstance() {
        return instance;
    }

    public void resetTemp(DailyForecastAnswer forecastAnswer) {
        for (int i = 0; i < DAYS_AMOUNT; i++) {
            temperatures[i] = (int) forecastAnswer.get(i).getTemp();
        }

        for (int i = 0; i < dayHolders.size(); i++) {
            String s = forecastAnswer.get(i).getTemp() + MainActivity.getInstance().getString(R.string.temperatureUnit);
            dayHolders.get(i).tempTextView.setText(s);
        }
    }

    public DaysAdapter() {
        instance = this;
    }

    @NonNull
    @Override

    public DayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final int ITEM_ID = R.layout.main_activity_list_item;

        View itemView = inflater.inflate(ITEM_ID, parent, false);

        DayHolder dayHolder = new DayHolder(itemView);

        daysCounter++;

        return dayHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder holder, int position) {
        String readyStr = numToStrDayOfWeek(daysIntegers[position]);
        holder.bind(readyStr, (String) dayHolders.get(position).tempTextView.getText());
/*        DayHolder old = dayHolders.get(position);
        dayHolders.set(position, holder);
        dayHolders.set(dayHolders.indexOf(holder), old);*/
    }

    @Override
    public int getItemCount() {
        return DAYS_AMOUNT;
    }

    class DayHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView tempTextView;

        public DayHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.list_text_top);
            tempTextView = itemView.findViewById(R.id.list_text_bottom);

            String s = temperatures[daysCounter] + MainActivity.getInstance().getString(R.string.temperatureUnit);
            tempTextView.setText(s);

            dayHolders.add(this);
        }

        void bind(String date, String temp) {
            dateTextView.setText(date);
            tempTextView.setText(temp);
        }
    }


    public void resetCurrentDay() {
        for (int i = 0; i < DAYS_AMOUNT; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_WEEK, i);
            daysIntegers[i] = calendar.get(Calendar.DAY_OF_WEEK);
        }
    }

    private String numToStrDayOfWeek(int num) {
        String readyStr;

        switch (num) {
            case 1:
                readyStr = "SUN";
                break;
            case 2:
                readyStr = "MON";
                break;
            case 3:
                readyStr = "TUE";
                break;
            case 4:
                readyStr = "WED";
                break;
            case 5:
                readyStr = "THU";
                break;
            case 6:
                readyStr = "FRI";
                break;
            case 7:
                readyStr = "SAT";
                break;
            default:
                readyStr = "UNDEFINED";
                break;
        }

        return readyStr;
    }
}
