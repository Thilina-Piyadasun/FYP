package org.abithana.ds;

import org.abithana.beans.PopulationBean;
import org.abithana.prescriptionBeans.LocationWeightBean;
import org.abithana.utill.Config;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilina on 12/19/2016.
 */
public class PopulationDataStore implements Serializable{

    private static DataFrame populationDF;
    private String populationtableName= "populationTbl";
    private String populationfilePath=null;
    private String stateCol=null;
    private String countyCol=null;
    private String tractCol=null;
    private String blockCol=null;
    private String populationCol=null;
    private String latitudeCol=null;
    private String longitudeCol=null;
    private static PopulationDataStore populationDataStore=new PopulationDataStore();
    private PopulationDataStore(){

    }
    Config instance=Config.getInstance();

    public static PopulationDataStore getInstance(){
        return populationDataStore;
    }

    public String[] getColumns(String filePath){

        populationfilePath=filePath;
        String[] colums = instance.getSqlContext().read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(filePath).columns();

        return colums;
    }

    public List<String> getUserRevisedColumns(){

        List<String> selectedColums=new ArrayList<>();

       /* if(stateCol!=null){
            selectedColums.add(stateCol);
        }if(countyCol!=null){
            selectedColums.add(countyCol);
        }if(tractCol!=null){
            selectedColums.add(tractCol);
        }if(blockCol!=null){
            selectedColums.add(blockCol);*/
        if(populationCol!=null){
            selectedColums.add(populationCol);
        }if(latitudeCol!=null){
            selectedColums.add(latitudeCol);
        }if(longitudeCol!=null){
            selectedColums.add(longitudeCol);
        }
        return selectedColums;

    }
    public DataFrame saveTable(String tableName){

        // Load the input data to a static Data Frame
        populationDF = instance.getSqlContext().read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(populationfilePath);

        //create temp table for get unique format
        String tmptbl=tableName+"_temp";
        populationDF.registerTempTable(tmptbl);

        List<String> list=getUserRevisedColumns();
        String exp="";

        for(int i=0;i<list.size();i++){
           if(i!=0) {
               exp = exp + ","+list.get(i);
           }else {
               exp = list.get(0);
           }
        }
        populationDF=instance.getSqlContext().sql("Select "+exp+" from "+tmptbl);

        List<PopulationBean> populationBeanList = populationDF.javaRDD().map(new Function<Row, PopulationBean>() {
            public PopulationBean call(Row row) {

                PopulationBean populationBean=new PopulationBean(row.getAs(populationCol),row.getAs(latitudeCol),row.getAs(longitudeCol));
                return populationBean;
            }
        }).collect();


        populationDF=instance.getSqlContext().createDataFrame(populationBeanList,PopulationBean.class);
        populationDF.show(50);

        populationDF.registerTempTable(tableName);
        //set table name for further use
        this.populationtableName=tableName;
        //drop registed tmp table
        instance.getSqlContext().dropTempTable(tmptbl);
        cache_data(1);
        return  populationDF;
    }

    private void cache_data(int storage_level){

        if(storage_level==1)
            populationDF.persist(StorageLevel.MEMORY_ONLY());
        else if(storage_level==2)
            populationDF.persist(StorageLevel.MEMORY_ONLY_SER());
        else if(storage_level==3)
            populationDF.persist(StorageLevel.MEMORY_AND_DISK());
        else
            populationDF.persist(StorageLevel.MEMORY_AND_DISK_SER());
    }

    public DataFrame getDataFrame(){
        return populationDF;
    }

    public String getTableName() {
        return populationtableName;
    }

    public String[] showColumns(String tableName){
        if(tableName==populationtableName)
            return populationDF.columns();
        else{
            return null;
        }
    }

    public String getLongitudeCol() {
        return longitudeCol;
    }

    public void setLongitudeCol(String longitudeCol) {
        this.longitudeCol = longitudeCol;
    }

    public String getLatitudeCol() {
        return latitudeCol;
    }

    public void setLatitudeCol(String latitudeCol) {
        this.latitudeCol = latitudeCol;
    }

    public String getPopulationCol() {
        return populationCol;
    }

    public void setPopulationCol(String populationCol) {
        this.populationCol = populationCol;
    }

    public String getBlockCol() {
        return blockCol;
    }

    public void setBlockCol(String blockCol) {
        this.blockCol = blockCol;
    }

    public String getTractCol() {
        return tractCol;
    }

    public void setTractCol(String tractCol) {
        this.tractCol = tractCol;
    }

    public String getCountyCol() {
        return countyCol;
    }

    public void setCountyCol(String countyCol) {
        this.countyCol = countyCol;
    }

    public String getStateCol() {
        return stateCol;
    }

    public void setStateCol(String stateCol) {
        this.stateCol = stateCol;
    }

    public String getPopulationtableName() {
        return populationtableName;
    }
}
