package org.abithana.ds;

import java.util.List;

/**
 * Created by Thilina on 1/19/2017.
 */
public class Test {

    CrimeDataStore dataStore=CrimeDataStore.getInstance();

    public static void main(String args[]){
        Test t=new Test();
        t.testGetColumns();
        t.testGetUserRevisedColumns();
        t.testSaveTable();
        t.testGetRDD();
    }

    public void testGetColumns()  {
        String s[]=dataStore.getColumns("D:\\FYP\\sample.csv");

        dataStore.setDatesCol("Dates");
        dataStore.setCategoryCol("Category");
        dataStore.setDayOfWeekCol("DayOfWeek");
        dataStore.setPdDistrictCol("PdDistrict");
        dataStore.setLatitudeCol("X");
        dataStore.setLongitudeCol("Y");
    }

    public void testGetUserRevisedColumns()  {

        List<String> s=dataStore.getUserRevisedColumns();
        for(String st:s){
            System.out.println(st);
        }
    }

    public void testSaveTable() {
        dataStore.saveTable("initialTable").show(30);
    }

    public void testGetRDD(){
        dataStore.getInitialRDD();
    }
}
