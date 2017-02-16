package org.abithana.statBeans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Thilina on 8/5/2016.
 */
public class CrimeDataBean implements Serializable {

    private Timestamp time;  //change this to int after preprocessor implement
    private String dayOfWeek;
    private String PD_District;
    private String Category;
    private double X;
    private double Y;

    public CrimeDataBean(Timestamp time, String dayOfWeek, String PD_District, String category, double x, double y) {
        this.time = time;
        this.dayOfWeek = dayOfWeek;
        this.PD_District = PD_District;
        Category = category;
        X = x;
        Y = y;
    }
}
