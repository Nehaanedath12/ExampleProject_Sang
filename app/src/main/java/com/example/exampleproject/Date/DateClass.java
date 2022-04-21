package com.example.exampleproject.Date;

public class DateClass {
    String day,month,year;
    int monthInt;

    public DateClass(String day, String month,int monthInt, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.monthInt=monthInt;
    }

    public int getMonthInt() {
        return monthInt;
    }

    public void setMonthInt(int monthInt) {
        this.monthInt = monthInt;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
