package org.abithana.preprocessor.impl;

import org.abithana.beans.CrimeDataBeanWithTime;
import org.abithana.beans.CrimeTestBeanWithTIme;
import org.abithana.beans.PopulationBean;
import org.abithana.ds.CrimeDataStore;
import org.abithana.ds.PopulationDataStore;
import org.abithana.utill.Config;
import org.abithana.utill.CrimeUtil;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by acer on 11/21/2016.
 */
public class Preprocessing implements Serializable{

    Config instance=Config.getInstance();
    String[] fetureSet[];
    int rowLimit;

    public DataFrame dropCol(DataFrame df,String column){

        try{

            df=df.drop(column);
        }catch (Exception e){

        }
        return df;

    }
    public Column getCol(DataFrame df,String column){

        Column col=null;
        try{
            col=df.col(column);
        }catch (Exception e){

        }
        return col;

    }
    public DataFrame aggregateDataFrames(DataFrame f1,DataFrame f2){
        return null;
        
    }

    public DataFrame stringIndexing(DataFrame df){
        return df;

    }

    public DataFrame createFeatureFrame(DataFrame f1){
        return f1;

    }

    public DataFrame removeDupliates(DataFrame f1){
        return f1;

    }

    public String[][] getFetureSet() {
        return fetureSet;
    }

    public void setFetureSet(String[][] fetureSet) {
        this.fetureSet = fetureSet;
    }

    public int getRowLimit() {
        return rowLimit;
    }

    public void setRowLimit(int rowLimit) {
        this.rowLimit = rowLimit;
    }

    /*for a givn data frame it indexed the time column 1-24
    * if time column exists it convet it to 1-24 if only ate column exists convert date into time and indexed
    * */
    public DataFrame getTimeIndexedDF(DataFrame df,String columnWithTime){

        DataFrame myDataframe=df;
        try{
            CrimeUtil crimeUtil=new CrimeUtil();
            boolean colexists=crimeUtil.isColExists(df,columnWithTime);

            if(colexists) {

                JavaRDD<CrimeDataBeanWithTime> crimeDataBeanJavaRDD = df.javaRDD().map(new Function<Row, CrimeDataBeanWithTime>() {
                    public CrimeDataBeanWithTime call(Row row) {

                        String s;
                        int year=0;
                        int month=0;
                        int day=0;

                        if(row.getAs("dateAndTime").getClass()== java.sql.Timestamp.class){
                           s = ""+row.getAs("dateAndTime").toString();
                            String dates[]=s.split(" ");
                            String year_mnth_day=dates[0];
                            //year= year_mnth_day.substring(0,4);
                            String year_string= year_mnth_day.substring(year_mnth_day.length()-4,year_mnth_day.length());
                            if(isNumeric(year_string)) {
                                year=Integer.parseInt(year_string);
                                String arr[] = year_mnth_day.split("/");
                                if(isNumeric(arr[0])) {
                                    month = Integer.parseInt(arr[0]);
                                    day = Integer.parseInt(arr[1]);
                                }
                            }
                            else{
                                if(isNumeric(year_mnth_day.substring(0,4)))
                                    year=Integer.parseInt(year_mnth_day.substring(0,4));
                                String arr[] = year_mnth_day.split("/");
                                if(isNumeric(arr[2])) {
                                    month = Integer.parseInt(arr[2]);
                                    day = Integer.parseInt(arr[1]);
                                }
                             }
                        }
                        else {
                            s = "" + row.getAs("dateAndTime");
                            String dates[] = s.split(" ");
                            String year_mnth_day = dates[0];

                            String year_string = year_mnth_day.substring(year_mnth_day.length() - 4, year_mnth_day.length());
                            if (isNumeric(year_string)) {
                                year = Integer.parseInt(year_string);
                                String arr[] = year_mnth_day.split("/");
                                if (isNumeric(arr[0])) {
                                    month = Integer.parseInt(arr[0]);
                                    day = Integer.parseInt(arr[1]);
                                }
                            } else {
                                if (isNumeric(year_mnth_day.substring(0, 4)))
                                    year = Integer.parseInt(year_mnth_day.substring(0, 4));
                                String arr[] = year_mnth_day.split("/");
                                if (isNumeric(arr[2])) {
                                    month = Integer.parseInt(arr[2]);
                                    day=Integer.parseInt(arr[1]);
                                }
                            }
                        }

                        Pattern pattern = Pattern.compile("(\\d{1,2})[:]\\d{1,2}[^:-]");
                        // Now create matcher object.
                        Matcher m = pattern.matcher(s);
                        int time = 0;

                        if (m.find()) {
                            time = Integer.parseInt(m.group(1));
                        }

                        CrimeDataBeanWithTime crimeDataBean;

                        if(row.getAs("category").equals("LARCENY/THEFT")||row.getAs("category").equals("NON-CRIMINAL")||row.getAs("category").equals("ASSAULT")|row.getAs("category").equals("VEHICLE THEFT")||row.getAs("category").equals("BURGLARY")||row.getAs("category").equals("VANDALISM")||row.getAs("category").equals("WARRANTS")||row.getAs("category").equals("SUSPICIOUS OCC")||row.getAs("category").equals("OTHER OFFENSES")){

                            crimeDataBean = new CrimeDataBeanWithTime(year,month,day,time,row.getAs("category"),row.getAs("dayOfWeek"),row.getAs("pdDistrict"),row.getAs("resolution"),row.getAs("latitude"),row.getAs("longitude"));
                        }
                        else{
                            crimeDataBean = new CrimeDataBeanWithTime(year,month,day,time,"MINOR CRIMES",row.getAs("dayOfWeek"),row.getAs("pdDistrict"),row.getAs("resolution"),row.getAs("latitude"),row.getAs("longitude"));
                        }
                         return crimeDataBean;
                    }
                });
                myDataframe = instance.getSqlContext().createDataFrame(crimeDataBeanJavaRDD, CrimeDataBeanWithTime.class);
                myDataframe.show(50);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return myDataframe;
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }
    /*for a givn data frame it indexed the time column 1-24
    * if time column exists it convet it to 1-24 if only ate column exists convert date into time and indexed
    * */
    public DataFrame getTimeIndexedDFforTest(DataFrame df,String columnWithTime){

        DataFrame myDataframe=df;
        try{
            CrimeUtil crimeUtil=new CrimeUtil();
            boolean colexists=crimeUtil.isColExists(df,columnWithTime);

            CrimeDataStore dataStore=CrimeDataStore.getInstance();

            if(colexists) {

                JavaRDD<CrimeTestBeanWithTIme> crimeTestBeanJavaRDD = df.javaRDD().map(new Function<Row, CrimeTestBeanWithTIme>() {
                    public CrimeTestBeanWithTIme call(Row row) {


                        String s;
                        int year=0;
                        int month=0;
                        int day=0;

                        if(row.getAs("Dates").getClass()== java.sql.Timestamp.class){
                            s = ""+row.getAs("Dates").toString();
                            String dates[]=s.split(" ");
                            String year_mnth_day=dates[0];
                            //year= year_mnth_day.substring(0,4);
                            String year_string= year_mnth_day.substring(year_mnth_day.length()-4,year_mnth_day.length());
                            if(isNumeric(year_string)) {
                                year=Integer.parseInt(year_string);
                                String arr[] = year_mnth_day.split("/");
                                if(isNumeric(arr[0])) {
                                    month = Integer.parseInt(arr[0]);
                                    day = Integer.parseInt(arr[1]);
                                }
                            }
                            else{
                                if(isNumeric(year_mnth_day.substring(0,4)))
                                    year=Integer.parseInt(year_mnth_day.substring(0,4));
                                String arr[] = year_mnth_day.split("/");
                                if(isNumeric(arr[2])) {
                                    month = Integer.parseInt(arr[2]);
                                    day = Integer.parseInt(arr[1]);
                                }
                            }
                        }
                        else {
                            s = "" + row.getAs("Dates");
                            String dates[] = s.split(" ");
                            String year_mnth_day = dates[0];

                            String year_string = year_mnth_day.substring(year_mnth_day.length() - 4, year_mnth_day.length());
                            if (isNumeric(year_string)) {
                                year = Integer.parseInt(year_string);
                                String arr[] = year_mnth_day.split("/");
                                if (isNumeric(arr[0])) {
                                    month = Integer.parseInt(arr[0]);
                                    day = Integer.parseInt(arr[1]);
                                }
                            } else {
                                if (isNumeric(year_mnth_day.substring(0, 4)))
                                    year = Integer.parseInt(year_mnth_day.substring(0, 4));
                                String arr[] = year_mnth_day.split("/");
                                if (isNumeric(arr[2])) {
                                    month = Integer.parseInt(arr[2]);
                                    day=Integer.parseInt(arr[1]);
                                }
                            }
                        }

                        Pattern pattern = Pattern.compile("(\\d{1,2})[:]\\d{1,2}[^:-]");
                        // Now create matcher object.
                        Matcher m = pattern.matcher(s);
                        int time = 0;

                        if (m.find()) {
                            time = Integer.parseInt(m.group(1));
                        }

                        CrimeTestBeanWithTIme crimeTestBean = new CrimeTestBeanWithTIme(year,month,day,time,row.getAs(dataStore.getDayOfWeekCol()),row.getAs(dataStore.getPdDistrictCol()),row.getAs(dataStore.getResolution()),row.getAs(dataStore.getLatitudeCol()),row.getAs(dataStore.getLongitudeCol()));

                        return crimeTestBean;
                    }
                });

                myDataframe = instance.getSqlContext().createDataFrame(crimeTestBeanJavaRDD, CrimeTestBeanWithTIme.class);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return myDataframe;
    }

    public DataFrame integratePopulationData(String populatinTableName,String preproessTblName){

        DataFrame crimeDf=instance.getSqlContext().sql("select * from "+preproessTblName);
        boolean isTestData=true;
        for(String s:crimeDf.columns()){
            if(s.equals("category")){
                isTestData=false;
            }
        }

        if(isTestData){
           return collectAsTestSet(populatinTableName,preproessTblName);
        }

         return  collectAsTrainSet(populatinTableName, preproessTblName);
    }

    public DataFrame collectAsTrainSet(String populatinTableName,String preproessTblName){
        try {
            DataFrame df = instance.getSqlContext().sql("SELECT t2.population,t1.dayOfWeek,t1.category,t1.pdDistrict,t1.time,t1.year,t1.month, t1.latitude,t1.longitude  FROM " + preproessTblName + "  t1 JOIN " + populatinTableName + " t2 ON  t1.latitude >= t2.latitude-0.01 AND  t1.latitude <= t2.latitude+0.01 and t1.longitude >= t2.longitude-0.01 AND t1.longitude <= t2.longitude+0.01");
            return df;
        }
        catch (Exception e){
            System.out.println("Exception in collectAsTrainSet method in Preprocessing");
            e.printStackTrace();
        }
        return null;
    }

    public DataFrame collectAsTestSet(String populatinTableName,String preproessTblName){
        try {
            DataFrame df = instance.getSqlContext().sql("SELECT t2.population,t1.dayOfWeek,t1.pdDistrict,t1.time,t1.month,t1.year, t1.latitude,t1.longitude  FROM " + preproessTblName + "  t1 JOIN " + populatinTableName + " t2 ON  t1.latitude >= t2.latitude-0.01 AND  t1.latitude <= t2.latitude+0.01 and t1.longitude >= t2.longitude-0.01 AND t1.longitude <= t2.longitude+0.01");
            return df;
        }
        catch (Exception e){
            System.out.println("Exception in collectAsTestSet method in Preprocessing");
            e.printStackTrace();
        }
        return null;
    }
}
