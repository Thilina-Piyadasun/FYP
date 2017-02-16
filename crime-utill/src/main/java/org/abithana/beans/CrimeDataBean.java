package org.abithana.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by acer on 11/20/2016.
 */
public class CrimeDataBean implements Serializable {

    private String dateAndTime;
    private String dayOfWeek;
    private String category;
    private String pdDistrict;
    private String resolution;
    private double latitude;
    private double longitude;

    public CrimeDataBean(String dateAndTime, String category,String dayOfWeek, String pdDistrict,String resolution, double latitude, double longitude) {
        this.dateAndTime=dateAndTime;
        this.dayOfWeek = dayOfWeek;
        this.category = category;
        this.pdDistrict = pdDistrict;
        this.resolution=resolution;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPdDistrict() {
        return pdDistrict;
    }

    public void setPdDistrict(String pdDistrict) {
        this.pdDistrict = pdDistrict;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
