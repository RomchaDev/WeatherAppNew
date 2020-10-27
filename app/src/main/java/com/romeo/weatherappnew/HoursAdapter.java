package com.romeo.weatherappnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HourViewHolder> {

    private final static int HOURS_AMOUNT = 24;
    private static int hoursCounter = 0;
    private final List<HourViewHolder> hours = new ArrayList<>();

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int listItemId = R.layout.main_activity_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        hoursCounter ++;

        HourViewHolder viewHolder = new HourViewHolder(inflater.inflate(listItemId, parent, false));

        viewHolder.hourView.setText(String.valueOf(hoursCounter));

        hours.add(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        String finalString;

        String inputString = String.valueOf(position);

        if (inputString.length() == 1)
            finalString = '0' + inputString + ":00";
        else
            finalString = inputString + ":00";

        holder.bind(finalString);
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

            tempView.setText("0°C");
        }

        private void bind(String hour) {
            hourView.setText(hour);
        }

        private void setTemp(int temp) {
            tempView.setText(String.valueOf(temp));
        }
    }
}
