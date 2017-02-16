package org.abithana.prescriptionBeans;

import java.io.Serializable;

/**
 * Created by Thilina on 12/28/2016.
 */
public class PresDataBean implements Serializable{

    private int categoryWeight;
    private double lat;
    private double lon;
    private int tractID;
    private double cellMidLat;
    private double cellMidLon;

    public PresDataBean(int categoryWeight, double lat, double lon, int tractID, double cellMidLat, double cellMidLon) {
        this.categoryWeight = categoryWeight;
        this.lat = lat;
        this.lon = lon;
        this.tractID = tractID;
        this.cellMidLat = cellMidLat;
        this.cellMidLon = cellMidLon;
    }

    public PresDataBean(int categoryWeight, double lat, double lon, int tractID) {
        this.categoryWeight = categoryWeight;
        this.lat = lat;
        this.lon = lon;
        this.tractID = tractID;
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

    public int getTractID() {
        return tractID;
    }

    public void setTractID(int tractID) {
        this.tractID = tractID;
    }

    public double getCellMidLat() {
        return cellMidLat;
    }

    public void setCellMidLat(double cellMidLat) {
        this.cellMidLat = cellMidLat;
    }

    public double getCellMidLon() {
        return cellMidLon;
    }

    public void setCellMidLon(double cellMidLon) {
        this.cellMidLon = cellMidLon;
    }
}
