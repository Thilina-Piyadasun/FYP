package org.abithana.statBeans;

import java.io.Serializable;

/**
 * Created by User on 9/22/2016.
 */
public class CrimeWithGrid implements Serializable {

    private int TimeIndex;
    private String DayOfWeek;
    private String Category;
    private String PdDistrict;
    private String Resolution;
    private double Grid;


    public CrimeWithGrid(int time, String category, String dayOfWeek, String pdDistrict,String resolution, double grid) {
        this.TimeIndex =time;
        this.DayOfWeek = dayOfWeek;
        this.Category = category;
        this.PdDistrict = pdDistrict;
        this.Resolution=resolution;
        this.Grid=grid;
    }

    public int getTimeIndex() {
        return TimeIndex;
    }

    public void setTimeIndex(int timeIndex) {
        TimeIndex = timeIndex;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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

    public double getGrid() {
        return Grid;
    }

    public void setGrid(double grid) {
        Grid = grid;
    }
}
