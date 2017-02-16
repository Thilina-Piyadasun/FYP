package org.abithana.ds;

import org.abithana.beans.CrimeDataBean;
import org.abithana.beans.PopulationBean;
import org.abithana.utill.Config;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilina on 11/19/2016.
 */
public class CrimeDataStore implements Serializable{

    private static DataFrame input;
    private String initailtableName;
    private String crimeFilePath=null;
    private static CrimeDataStore crimeDataStore=new CrimeDataStore();
    private Config instance= Config.getInstance();


    private String categoryCol=null;
    private String dayOfWeekCol =null;
    private String pdDistrictCol =null;
    private String latitudeCol=null;
    private String longitudeCol=null;
    private String datesCol =null;
    private String resolution=null;

    private CrimeDataStore(){

    }

    public String[] getColumns(String filePath){

        this.crimeFilePath=filePath;
        String[] colums = instance.getSqlContext().read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(filePath).columns();

        return colums;
    }

    public List<String> getUserRevisedColumns(){

        List<String> selectedColums=new ArrayList<>();

        if(dayOfWeekCol !=null){
            selectedColums.add(dayOfWeekCol);
        }if(pdDistrictCol !=null){
            selectedColums.add(pdDistrictCol);
        }if(datesCol !=null) {
            selectedColums.add(datesCol);
        }if(resolution!=null){
            selectedColums.add(resolution);
        }
        if(categoryCol!=null){
            selectedColums.add(categoryCol);
        }if(latitudeCol!=null){
            selectedColums.add(latitudeCol);
        }if(longitudeCol!=null){
            selectedColums.add(longitudeCol);
        }
        return selectedColums;

    }
    public static CrimeDataStore getInstance(){
        return crimeDataStore;
    }

    public DataFrame saveTable(String tableName){

        // Load the input data to a static Data Frame
        input = instance.getSqlContext().read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(crimeFilePath);

        //create temp table for get unique format
        String tmptbl=tableName+"_temp";
        input.registerTempTable(tmptbl);

        List<String> list=getUserRevisedColumns();
        String exp="";

        for(int i=0;i<list.size();i++){
            if(i!=0) {
                exp = exp + ","+list.get(i);
            }else {
                exp = list.get(0);
            }
        }
        input=instance.getSqlContext().sql("Select "+exp+" from "+tmptbl);

        List<CrimeDataBean> crimeDataBeanList = input.javaRDD().map(new Function<Row, CrimeDataBean>() {
            public CrimeDataBean call(Row row) {

                CrimeDataBean crimeDataBean=new CrimeDataBean(""+row.getAs(datesCol),row.getAs(categoryCol),row.getAs(dayOfWeekCol),row.getAs(pdDistrictCol),row.getAs(resolution),row.getAs(latitudeCol),row.getAs(longitudeCol));
                return crimeDataBean;
            }
        }).collect();


        input=instance.getSqlContext().createDataFrame(crimeDataBeanList,CrimeDataBean.class);
        input.show(50);

        input.registerTempTable(tableName);
        //set table name for further use
        initailtableName=tableName;
        //drop registed tmp table
        instance.getSqlContext().dropTempTable(tmptbl);
        cache_data(1);
        return  input;
    }


    private void cache_data(int storage_level){

        if(storage_level==1)
            input.persist(StorageLevel.MEMORY_ONLY());
        else if(storage_level==2)
            input.persist(StorageLevel.MEMORY_ONLY_SER());
        else if(storage_level==3)
            input.persist(StorageLevel.MEMORY_AND_DISK());
        else
            input.persist(StorageLevel.MEMORY_AND_DISK_SER());
    }

    public String getCategoryCol() {
        return categoryCol;
    }

    public void setCategoryCol(String categoryCol) {
        this.categoryCol = categoryCol;
    }

    public String getDayOfWeekCol() {
        return dayOfWeekCol;
    }

    public void setDayOfWeekCol(String dayOfWeekCol) {
        this.dayOfWeekCol = dayOfWeekCol;
    }

    public String getPdDistrictCol() {
        return pdDistrictCol;
    }

    public void setPdDistrictCol(String pdDistrictCol) {
        this.pdDistrictCol = pdDistrictCol;
    }

    public String getLatitudeCol() {
        return latitudeCol;
    }

    public void setLatitudeCol(String latitudeCol) {
        this.latitudeCol = latitudeCol;
    }

    public String getLongitudeCol() {
        return longitudeCol;
    }

    public void setLongitudeCol(String longitudeCol) {
        this.longitudeCol = longitudeCol;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDatesCol() {
        return datesCol;
    }

    public void setDatesCol(String datesCol) {
        this.datesCol = datesCol;
    }

    public JavaRDD getInitialRDD(){
        return getRDD(initailtableName);
    }

    private JavaRDD getRDD(String tableName){

        try{
            DataFrame rdd=instance.getSqlContext().sql("Select dateAndTime,dayOfWeek, pdDistrict, category,resolution, lattitude, longitude from " + tableName);
            JavaRDD<CrimeDataBean> crimeDataBeanJavaRDD = rdd.javaRDD().map(new Function<Row, CrimeDataBean>() {
                public CrimeDataBean call(Row row) {
                    CrimeDataBean crimeDataBean = new CrimeDataBean(row.getAs("dateAndTime"),row.getAs("categoryCol"),row.getAs("dayOfWeek"),row.getAs("pdDistrict"),row.getAs("resolution"),row.getAs("lattitude"),row.getAs("longitude"));
                    return crimeDataBean;
                }
            });
            return crimeDataBeanJavaRDD;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public  JavaRDD<Vector> getDataVector(String tableName){

        try{
            DataFrame rdd=instance.getSqlContext().sql("Select  lattitude, longitude from " + tableName);

            JavaRDD<Vector> crimeDataBeanJavaRDD = rdd.javaRDD().map(new Function<Row, Vector>() {
                public Vector call(Row row) {
                    return Vectors.dense(row.getDouble(0),row.getDouble(1));
                }
            });
            return crimeDataBeanJavaRDD;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public List<Row> getList(String sqlQuery){

        try{
            return instance.getSqlContext().sql(sqlQuery).collectAsList();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public DataFrame queryDataSet(String sqlQuery){

        try{
            DataFrame df=instance.getSqlContext().sql(sqlQuery);
            return df;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public String[] showColumns(String tableName){
        if(tableName==initailtableName)
            return input.columns();
        else{
            return null;
        }
    }

    public DataFrame getDataFrame(){
        return input;
    }


    public String getTableName() {
        return initailtableName;
    }

    public void setTableName(String initailtableName) {
        this.initailtableName = initailtableName;
    }

    public String getCrimeFilePath() {
        return crimeFilePath;
    }

    public void setCrimeFilePath(String crimeFilePath) {
        this.crimeFilePath = crimeFilePath;
    }

    public String getInitailtableName() {
        return initailtableName;
    }

    public void setInitailtableName(String initailtableName) {
        this.initailtableName = initailtableName;
    }

    public long getRowCount(){
        if(input!=null)
            return input.count();
        else
            return 0;
    }
}
