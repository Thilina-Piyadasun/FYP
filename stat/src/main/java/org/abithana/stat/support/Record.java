package org.abithana.stat.support;

import java.util.ArrayList;

/**
 * Created by minudika on 8/6/16.
 */
public class Record {
    private int length = 0;
    ArrayList<Object>data = new ArrayList<Object>();

    public Record(int length){
        this.length = length;
    }

    public void addEntry(Object entry){
        if(data.size() < length) {
            data.add(entry);
        }

        //TODO : Array list out of bound exception
    }

    public ArrayList<Object> getData(){
        return data;
    }
}
