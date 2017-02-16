package org.abithana.ds;

import org.abithana.beans.DataStoreBeans;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.util.List;

/**
 * Created by Thilina on 11/30/2016.
 */
public class PreprocessedCrimeDataStore implements DataStore {

    private static DataFrame preprocessedDf=null;
    private String prepTableName="preprocessedData";
    private static PreprocessedCrimeDataStore crimeDataStore=new PreprocessedCrimeDataStore();

    private PreprocessedCrimeDataStore(){

    }

    public static PreprocessedCrimeDataStore getInstance(){
        return crimeDataStore;
    }



    @Override
    public DataFrame queryDataSet(String sqlQuery) {
        try{

            DataFrame df=sqlContext.sql(sqlQuery);
            return df;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public void saveTable(DataFrame df, String tableName) {
        preprocessedDf=df;
        prepTableName=tableName;
        preprocessedDf.registerTempTable(tableName);
        cache_data(1);
    }

    @Override
    public String[] showColumns(String tableName) {
        if(tableName==prepTableName){
            return preprocessedDf.columns();
        }
        else{
            return null;
        }
    }

    @Override
    public void read_file(String filename, int storage_level) {

    }

    @Override
    public void read_file(String filename, String tbleName) {

    }

    @Override
    public DataFrame getDataFrame() {
        return preprocessedDf;
    }

    @Override
    public JavaRDD<DataStoreBeans> getRDD(String tableName) {
        return null;
    }

    @Override
    public JavaRDD<Vector> getDataVector() {
        return null;
    }

    @Override
    public List<Row> getList(String sqlQuery) {
        return null;
    }

    private void cache_data(int storage_level){

        if(storage_level==1)
            preprocessedDf.persist(StorageLevel.MEMORY_ONLY());
        else if(storage_level==2)
            preprocessedDf.persist(StorageLevel.MEMORY_ONLY_SER());
        else if(storage_level==3)
            preprocessedDf.persist(StorageLevel.MEMORY_AND_DISK());
        else
            preprocessedDf.persist(StorageLevel.MEMORY_AND_DISK_SER());
    }

    public String getTableName() {
        return prepTableName;
    }

    public void setTableName(String prepTableName) {
        this.prepTableName = prepTableName;
    }

    public DataFrame readCsv(String filename){

        // Load the input data to a static Data Frame
        DataFrame df= org.abithana.utill.Config.getInstance().getSqlContext().read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(filename);

        return  df;
    }
}
