package org.abithana.frontConnector;

import org.abithana.ds.CrimeDataStore;

/**
 * Created by Thilina on 1/24/2017.
 */
public class Visualizer_UploadCrime {

    CrimeDataStore initaldataStore =CrimeDataStore.getInstance();

    /*
    * This returns all coloums in dataset uploaded by the user
    * return [] use to specify column names in the dataset
    * */
    public String[] getColums(String path){
        return initaldataStore.getColumns(path);
    }

    /*
    * Save each uploaded crime Data file
    * */
    public boolean saveTable(String tableName,String dates,String category,String dayOfWeek,String pdDistrict,String resolution,String lat,String lon){
        try {
            initaldataStore.setDatesCol(dates);
            initaldataStore.setCategoryCol(category);
            initaldataStore.setDayOfWeekCol(dayOfWeek);
            initaldataStore.setPdDistrictCol(pdDistrict);
            initaldataStore.setResolution(resolution);
            initaldataStore.setLatitudeCol(lat);
            initaldataStore.setLongitudeCol(lon);
            initaldataStore.saveTable(tableName).show(30);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public String getFileName(){
        return initaldataStore.getCrimeFilePath();
    }

    public String getTableName(){
        return initaldataStore.getInitailtableName();
    }
}
