package org.abithana.beans;

import java.io.Serializable;

/**
 * Created by Thilina on 12/16/2016.
 */
public class CrimeTestBeanWithTIme implements Serializable {


    private int year;
    private int month;
    private int day;
    private int Time;
    private String DayOfWeek;
    private String PdDistrict;
    private String Resolution;
    private double latitude;
    private double longitude;

    public CrimeTestBeanWithTIme(int year,int month,int day,int time, String dayOfWeek, String pdDistrict,String resolution, double latitude, double longitude) {
        this.year=year;
        this.month=month;
        this.day=day;
        this.Time =time;
        this.DayOfWeek = dayOfWeek;
        this.PdDistrict = pdDistrict;
        this.Resolution=resolution;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public String getPdDistrict() {
        return PdDistrict;
    }

    public void setPdDistrict(String pdDistrict) {
        PdDistrict = pdDistrict;
    }

    public String getResolution() {
        return Resolution;
    }

    public void setResolution(String resolution) {
        Resolution = resolution;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
