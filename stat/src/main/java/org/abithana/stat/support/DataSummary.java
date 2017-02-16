package org.abithana.stat.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by minudika on 8/5/16.
 */
public class DataSummary implements Serializable{
    private String baseField = null;
    private ArrayList subfields = null;
    private HashMap<String,ArrayList>dataEntries = null;
    private List<ArrayList> records;

    public DataSummary(){
        subfields = new ArrayList<String>();
        records = new ArrayList<ArrayList>();
    }

    public DataSummary(String... fields){
        this.baseField = fields[0];
        records = new ArrayList<ArrayList>();
        subfields = new ArrayList<String>();
        for(int i=1; i<fields.length; i++){
            subfields.add(fields[i]);
        }
    }

    public void setRecords(List<ArrayList> records){
        this.records = records;
    }

    public List<ArrayList> getRecords() {
        return records;
    }

    public void setBaseField(String baseField){
        this.baseField = baseField;
    }

    public String getBaseField(){
        return baseField;
    }

    public void addSubfield(String field){
        if(!subfields.contains(field)) {
            dataEntries.put(field, new ArrayList<Object>());
        }
        else{
            // TODO: throw error
        }
    }

    public void addSubfields(String... fields){
        for(String field : fields){
            subfields.add(field);
        }
    }
}
