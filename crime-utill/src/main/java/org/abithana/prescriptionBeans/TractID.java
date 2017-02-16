package org.abithana.prescriptionBeans;

import java.io.Serializable;

/**
 * Created by Thilina on 1/4/2017.
 */
public class TractID implements Serializable {

    private int tractID;

    public TractID(int tractID) {
        this.tractID = tractID;
    }

    public int getTractID() {
        return tractID;
    }

    public void setTractID(int tractID) {
        this.tractID = tractID;
    }
}
