package org.abithana.beans;

/**
 * Created by Thilina on 12/20/2016.
 */
public class PopulationCrimeBean {
    private int year;
    private int Time;
    private String DayOfWeek;
    private String Category;
    private String PdDistrict;

    private double X;
    private double Y;
    private int population;

    public PopulationCrimeBean(int year, int time, String dayOfWeek, String category, String pdDistrict,  double x, double y, int population) {
        this.year = year;
        Time = time;
        DayOfWeek = dayOfWeek;
        Category = category;
        PdDistrict = pdDistrict;

        X = x;
        Y = y;
        this.population = population;
    }

    public int getYear() {
        return year;
    }

    public int getTime() {
        return Time;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public String getCategory() {
        return Category;
    }

    public String getPdDistrict() {
        return PdDistrict;
    }



    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public int getPopulation() {
        return population;
    }
}
