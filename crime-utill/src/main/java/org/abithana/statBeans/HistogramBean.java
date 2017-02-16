package org.abithana.statBeans;

import java.io.Serializable;

/**
 * Created by Thilina on 8/28/2016.
 */
public class HistogramBean implements Serializable{

    private String label;
    private int frquncy;

    public HistogramBean(String label, long frquncy) {
        this.label = label;
        this.frquncy =(int)frquncy;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getFrquncy() {
        return frquncy;
    }

    public void setFrquncy(int frquncy) {
        this.frquncy = frquncy;
    }
}
