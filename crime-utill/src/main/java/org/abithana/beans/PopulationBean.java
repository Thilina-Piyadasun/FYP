package org.abithana.beans;

import java.io.Serializable;

/**
 * Created by Thilina on 12/19/2016.
 */
public class PopulationBean implements Serializable {

    private  int population;
    private  double latitude;
    private  double longitude;

    public PopulationBean(int population, double latitude, double longitude) {
        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
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
}
