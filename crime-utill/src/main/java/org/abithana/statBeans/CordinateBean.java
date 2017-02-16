package org.abithana.statBeans;

import java.io.Serializable;

/**
 * Created by minudika on 11/24/16.
 */
public class CordinateBean implements Serializable{

    private double x;
    private double y;

    public CordinateBean(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
