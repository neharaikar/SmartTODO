package com.example.project_testing;

public class Item_Selected {
    String item="";
    String time="";
    String place="";
    String date="";

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public Item_Selected(String item, String time, String place, String date) {
        this.item = item;
        this.time = time;
        this.place = place;
        this.date = date;
    }
}
