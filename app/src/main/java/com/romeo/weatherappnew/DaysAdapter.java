package com.romeo.weatherappnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayHolder> {

    private static final int DAYS_AMOUNT = 7;

    private static int daysCounter = 0;

    private static final List<DayHolder> days = new ArrayList<>();

    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final int ITEM_ID = R.layout.main_activity_list_item;

        View itemView = inflater.inflate(ITEM_ID, parent, false);

        DayHolder dayHolder = new DayHolder(itemView);

        daysCounter++;

        days.add(dayHolder);

        return dayHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder holder, int position) {
        holder.bind(String.valueOf(Calendar.getInstance().getTime().getDay()));
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

            tempTextView.setText("0°C");
        }

        void bind(String date) {
            dateTextView.setText(date);
        }
    }


}
