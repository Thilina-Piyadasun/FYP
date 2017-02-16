package org.abithana.statBeans;

import java.io.Serializable;

/**
 * Created by Thilina on 8/28/2016.
 */
public class HistogramBeanDouble implements Serializable{

    private String label;
    private double frquncy;

    public HistogramBeanDouble(String label, double frquncy) {
        this.label = label;
        this.frquncy =(int)frquncy;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getFrquncy() {
        return frquncy;
    }

    public void setFrquncy(double frquncy) {
        this.frquncy = frquncy;
    }
}
