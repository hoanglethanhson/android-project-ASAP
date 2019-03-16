package com.example.assignment.Entity;
import com.example.assignment.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class WeatherViewHolder extends RecyclerView.ViewHolder{
    public TextView itemDate;
    public TextView itemTemperature;
    public TextView itemIcon;
    public View lineView;

    public WeatherViewHolder(View view) {
        super(view);
        this.itemDate = view.findViewById(R.id.itemDate);
        this.itemTemperature = view.findViewById(R.id.itemTemperature);
        this.itemIcon = view.findViewById(R.id.itemIcon);
        this.lineView = view.findViewById(R.id.lineView);
    }
}

