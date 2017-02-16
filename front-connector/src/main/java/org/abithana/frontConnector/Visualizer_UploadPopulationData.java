package org.abithana.frontConnector;

import org.abithana.ds.CrimeDataStore;
import org.abithana.ds.PopulationDataStore;

/**
 * Created by Thilina on 1/24/2017.
 */
public class Visualizer_UploadPopulationData {

    PopulationDataStore populationDataStore =PopulationDataStore.getInstance();

    /*
    * This returns all coloums in dataset uploaded by the user
    * return [] use to specify column names in the dataset
    * */
    public String[] getColums(String path){
        return populationDataStore.getColumns(path);
    }

    /*
    * Save each uploaded population Data file
    * */
    public boolean saveTable(String tableName,String population,String lat,String lon){
        try {
            populationDataStore.setPopulationCol(population);
            populationDataStore.setLatitudeCol(lat);
            populationDataStore.setLongitudeCol(lon);
            populationDataStore.saveTable(tableName).show(30);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
