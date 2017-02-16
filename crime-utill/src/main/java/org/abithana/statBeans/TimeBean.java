package org.abithana.statBeans;

import java.io.Serializable;

/**
 * Created by Thilina on 8/28/2016.
 */
public class TimeBean implements Serializable {

    int time;

    public TimeBean(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
