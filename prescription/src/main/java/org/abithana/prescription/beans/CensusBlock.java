package org.abithana.prescription.beans;

import com.vividsolutions.jts.geom.Polygon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by malakaganga on 1/26/17.
 */
public class CensusBlock implements Serializable {

    private long censusId;
    private long population;
    private double midLatitude;
    private double midLongitude;
    private ArrayList<Double> polygonLatPoints = new ArrayList<Double>();
    private ArrayList<Double> polygonLonPoints = new ArrayList<Double>();
    private Polygon blockPolygon;

    public double getMidLatitude() {
        return midLatitude;
    }

    public void setMidLatitude(double midLatitude) {
        this.midLatitude = midLatitude;
    }

    public double getMidLongitude() {
        return midLongitude;
    }

    public void setMidLongitude(double midLongitude) {
        this.midLongitude = midLongitude;
    }

    public long getCensusId() {
        return censusId;
    }

    public void setCensusId(long censusId) {
        this.censusId = censusId;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public ArrayList<Double> getPolygonLatPoints() {
        return polygonLatPoints;
    }

    public void setPolygonLatPoints(ArrayList<Double> polygonLatPoints) {
        this.polygonLatPoints = polygonLatPoints;
    }

    public ArrayList<Double> getPolygonLonPoints() {
        return polygonLonPoints;
    }

    public void setPolygonLonPoints(ArrayList<Double> polygonLonPoints) {
        this.polygonLonPoints = polygonLonPoints;
    }

    public Polygon getBlockPolygon() {
        return blockPolygon;
    }

    public void setBlockPolygon(Polygon blockPolygon) {
        this.blockPolygon = blockPolygon;
    }
}
