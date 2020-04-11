package com.czb.ccparkinglot.record;

import java.io.Serializable;

public class Date implements Serializable {
    int year;
    int month;
    int day;
    int hour;
    int minute;

    public Date() {
    }

    public Date(int hour, int minute) {

        this.hour = hour;
        this.minute = minute;

    }

    public Date(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }


    @Override
    public String toString() {
        StringBuilder date = new StringBuilder();

        if (year == 0)
            if (month == 0 && day == 0)
                date.append(hour).append(":").append(minute);
            else date.append(month).append("月").append(day).append("日 ").append(hour).append(":").append(minute);
        else
            date.append(year).append("年").append(month).append("月").append(day).append("日 ").append(hour).append(":")
                    .append(minute);
        return date.toString();
    }
}
