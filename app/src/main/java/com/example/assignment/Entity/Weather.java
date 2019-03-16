package com.example.assignment.Entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Weather {



    private String city;
    private String country;
    private Date date;
    private String temperature;
    private String id;
    private String icon;
    private String lastUpdated;
    private Date sunrise;
    private Date sunset;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }



    public Date getSunrise(){
        return this.sunrise;
    }

    public void setSunrise(String dateString) {
        try {
            setSunrise(new Date(Long.parseLong(dateString) * 1000));
        }
        catch (Exception e) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
                setSunrise(inputFormat.parse(dateString));
            }
            catch (ParseException e2) {
                setSunrise(new Date()); // make the error somewhat obvious
                e2.printStackTrace();
            }
        }
    }

    public void setSunrise(Date date) {
        this.sunrise = date;
    }

    public Date getSunset(){
        return this.sunset;
    }

    public void setSunset(String dateString) {
        try {
            setSunset(new Date(Long.parseLong(dateString) * 1000));
        }
        catch (Exception e) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
                setSunrise(inputFormat.parse(dateString));
            }
            catch (ParseException e2) {
                setSunset(new Date()); // make the error somewhat obvious
                e2.printStackTrace();
            }
        }
    }

    public void setSunset(Date date) {
        this.sunset = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getDate(){
        return this.date;
    }

    public void setDate(String dateString) {
        try {
            setDate(new Date(Long.parseLong(dateString) * 1000));
        }
        catch (Exception e) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
                setDate(inputFormat.parse(dateString));
            }
            catch (ParseException e2) {
                setDate(new Date()); // make the error somewhat obvious
                e2.printStackTrace();
            }
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}