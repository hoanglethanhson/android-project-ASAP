package com.example.assignment.utils;

import android.content.SharedPreferences;

public class UnitConvertor {
    public static float convertTemperature(float temperature, SharedPreferences sp) {
        if (sp.getString("unit", "°C").equals("°C")) {
            return UnitConvertor.kelvinToCelsius(temperature);

        } else {
            return temperature;
        }
    }

    public static float kelvinToCelsius(float kelvinTemp) {
        return kelvinTemp - 273.15f;
    }

}
