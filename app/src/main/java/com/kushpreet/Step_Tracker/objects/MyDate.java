package com.kushpreet.Step_Tracker.objects;

public class MyDate {
    private int year;
    private int month;
    private int day;

    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String toString() {
        return String.format("%02d-%02d-%04d", day, month, year);
    }

    public int getYear() {
        return this.year;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public void setYear(int newYear) {
        this.year = newYear;
    }

    public void setMonth(int newMonth) {
        this.month = newMonth;
    }

    public void setDay(int newDay) {
        this.day = newDay;
    }
}
