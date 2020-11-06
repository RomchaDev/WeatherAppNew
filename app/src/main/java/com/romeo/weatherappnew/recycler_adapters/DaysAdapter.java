package com.romeo.weatherappnew.recycler_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.romeo.weatherappnew.JSON.UniversalForecastAnswer;
import com.romeo.weatherappnew.R;
import com.romeo.weatherappnew.activities.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayHolder> {

    private static final int DAYS_AMOUNT = 7;
    private static int daysCounter = 0;
    private static final List<DayHolder> dayHolders = new ArrayList<>();
    private final int[] daysIntegers = new int[DAYS_AMOUNT];
    private final int[] temperatures = new int[DAYS_AMOUNT];
    private final String[] weatherLabels = new String[DAYS_AMOUNT];

    public void resetForecastUniversal(UniversalForecastAnswer universalAnswer) {
        for (int i = 0; i < DAYS_AMOUNT; i++) {
            temperatures[i] = (int) universalAnswer.getDay(i).getTemp();
            weatherLabels[i] = universalAnswer.getDay(i).getWeather().getIcon();
        }

        for (int i = 0; i < dayHolders.size(); i++) {
            String s = universalAnswer.getDay(i).getTemp() + MainActivity.getInstance().getString(R.string.temperature_unit);
            dayHolders.get(i).tempTextView.setText(s);
            dayHolders.get(i).setImage(universalAnswer.getDay(i).getWeather().getIcon());
        }
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
        holder.bind(readyStr, (String) dayHolders.get(position).tempTextView.getText(), weatherLabels[position]);
    }

    @Override
    public int getItemCount() {
        return DAYS_AMOUNT;
    }

    class DayHolder extends RecyclerView.ViewHolder {

        private final TextView dateTextView;
        private final TextView tempTextView;
        private final ImageView weatherImage;


        public DayHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.list_text_top);
            tempTextView = itemView.findViewById(R.id.list_text_bottom);
            weatherImage = itemView.findViewById(R.id.main_list_image);

            String s = temperatures[daysCounter] + MainActivity.getInstance().getString(R.string.temperature_unit);
            tempTextView.setText(s);
            setImage(weatherLabels[daysCounter]);

            dayHolders.add(this);
        }

        void bind(String date, String temp, String label) {
            dateTextView.setText(date);
            tempTextView.setText(temp);
            setImage(label);
        }

        private void setImage(String label) {
            Picasso.get().load(String.format(MainActivity.IMAGE_URI, label)).into(weatherImage);
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
