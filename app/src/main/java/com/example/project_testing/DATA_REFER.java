package com.example.project_testing;

import android.app.Application;

public class DATA_REFER {
    String item="";
    String place="";
    String date="";
    String time="";

    public DATA_REFER(String item, String place, String date, String time) {
        this.item = item;
        this.place = place;
        this.date = date;
        this.time = time;
    }

    public DATA_REFER(){}
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
