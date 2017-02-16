package org.abithana.prescriptionBeans;

import java.io.Serializable;

/**
 * Created by Thilina on 1/3/2017.
 */
public class PrescriptionDataBean implements Serializable{

    private int categoryWeight;
    private double lat;
    private double lon;
    private long blockID;
    private long tractID;

    public PrescriptionDataBean(int categoryWeight, double lat, double lon, long blockID,long tractID) {
        this.categoryWeight = categoryWeight;
        this.lat = lat;
        this.lon = lon;
        this.blockID=blockID;
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

    public long getTractID() {
        return tractID;
    }

    public void setTractID(long tractID) {
        this.tractID = tractID;
    }

    public long getBlockID() {
        return blockID;
    }

    public void setBlockID(long blockID) {
        this.blockID = blockID;
    }
}
