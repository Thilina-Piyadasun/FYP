package org.abithana.prescriptionBeans;

import java.io.Serializable;

/**
 * Created by Thilina on 1/4/2017.
 */
public class LocationWeightBean implements Serializable {

    private int categoryWeight;
    private double lat;
    private double lon;

    public LocationWeightBean(int categoryWeight, double lat, double lon) {
        this.categoryWeight = categoryWeight;
        this.lat = lat;
        this.lon = lon;
    }

    public int getCategoryWeight() {
        return categoryWeight;
    }

    public void setCategoryWeight(int categoryWeight) {
        this.categoryWeight = categoryWeight;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
