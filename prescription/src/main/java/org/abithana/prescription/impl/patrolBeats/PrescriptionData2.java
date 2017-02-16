package org.abithana.prescription.impl.patrolBeats;

import org.abithana.prescription.beans.CensusBlock;
import org.abithana.prescription.impl.Redistricting.DistrictBoundryDefiner;
import org.abithana.prescriptionBeans.PrescriptionDataBean;
import org.abithana.utill.Config;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Thilina on 12/28/2016.
 */
public class PrescriptionData2 implements Serializable{

    private final int DISTANCE_GAP_IN_METERS=500; //in meters

    private DataFrame prescriptionDs;
    private String lat;
    private String lon;
    private String category;
    private Config instance=Config.getInstance();
    DistrictBoundryDefiner districtBoundryDefiner;
    private int N1;
    private int N2;

    private double dLat;
    private double dLon;
    HashMap<Long, HashSet<Long>> tractsOfDistricts=new HashMap<>();
    HashMap<Long, List<PrescriptionDataBean>> prescriptionDataMap=new HashMap<>();

    public DataFrame createPrescriptionDs(DistrictBoundryDefiner districtBoundryDefiner,Long districtId,String prescriptionTable,String patrolQuery){
        this.districtBoundryDefiner=districtBoundryDefiner;
        tractsOfDistricts=districtBoundryDefiner.getTractsOfDistricts();
        DataFrame df=instance.getSqlContext().sql(patrolQuery);
        df.registerTempTable(prescriptionTable);
        integrateTractID(districtId,prescriptionTable);
        return df;
    }
   /*
   * 0- defult
   * 1- weekdays
   * 2- weekends
   * */
    public String patrolQueryGenerator(String datasetUsing,int weekdays,int watchId){
        try {
            String query="Select *  from " + datasetUsing ;
            if(weekdays==1){
                query=query+ " where dayOfWeek not LIKE 'SAT%' and dayOfWeek not like 'SUN%' ";
                query=query+"and " +filterTime(watchId);
            }
            else if(weekdays==2) {
                query=query+" where dayOfWeek LIKE 'SAT%' or dayOfWeek  like 'SUN%' ";
                query=query+"and " + filterTime(watchId);
            }
            if(weekdays==0){
                query=query+ " where ";
                query=query+ filterTime(watchId);
            }

            return query;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String filterTime(int id){
        String timeFIlter="";
        if(id==0){
            timeFIlter = "1=1";
        }
        if(id==1)
            timeFIlter="  time > 3 and time <= 12 ";
        if(id==2)
            timeFIlter="  time > 11 and time <= 20 ";
        if(id==3)
            timeFIlter=" ((time > 21 and time <= 24) or (time >=0 and time <=4)) ";

        return timeFIlter;
    }

    public DataFrame integrateTractID(Long districtId,String prescriptionTable){

        Checker ch = new Checker();

        HashSet<Long> tractSet=new HashSet<>();
        tractSet=tractsOfDistricts.get(districtId);

        String s="-1";
        for(Long l:tractSet){
            s=s+","+l;
        }

        System.out.println(s);
        DataFrame df=instance.getSqlContext().sql("Select category,latitude,longitude from " +prescriptionTable);
        instance.getSqlContext().dropTempTable(prescriptionTable);

        List<PrescriptionDataBean> prescritptionDataJavaRDD = df.javaRDD().map(new Function<Row, PrescriptionDataBean>() {
            public PrescriptionDataBean call(Row row) {

                int weight = getWeightForCategory(row.getAs("category"));
                double lat = row.getAs("latitude");
                double lon = row.getAs("longitude");
                double[] array = {lon,lat};
                long blockID = ch.polygonChecker(array);

                long tractID=blockID/10000;

                PrescriptionDataBean prescritptionData = new PrescriptionDataBean(weight, lat, lon, blockID,tractID);
                return prescritptionData;
            }
        }).collect();

        HashMap<Long,CensusBlock> map=ch.getCensusMap();
        List<PrescriptionDataBean> prescritptionDataJavaRDD2=new ArrayList<>();

        //TODO improve the efficiency

        for(Long l:map.keySet()){
            PrescriptionDataBean prescritptionData = new PrescriptionDataBean(0, map.get(l).getMidLatitude(), map.get(l).getMidLongitude(), l,l/10000);
            prescritptionDataJavaRDD2.add(prescritptionData);
        }

        prescritptionDataJavaRDD2.addAll(prescritptionDataJavaRDD);

        DataFrame dataFrame = Config.getInstance().getSqlContext().createDataFrame(prescritptionDataJavaRDD2, PrescriptionDataBean.class);;
        dataFrame.registerTempTable(prescriptionTable);
        dataFrame=instance.getSqlContext().sql("Select * from " + prescriptionTable + " where tractID in (" + s + ")");
        instance.getSqlContext().dropTempTable(prescriptionTable);

        dataFrame.registerTempTable(prescriptionTable);
        return dataFrame;
    }


    private double distanceInMeters(double lat1, double lat2, double lon1,
        double lon2) {

            final int R = 6371; // Radius of the earth

            Double latDistance = Math.toRadians(lat2 - lat1);
            Double lonDistance = Math.toRadians(lon2 - lon1);
            Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000; // convert to meters
            //return Math.sqrt(distance);
        return distance;
    }
    public int getWeightForCategory(String category){

        Map<String,Integer> categoryWeight=new HashMap<>();
        categoryWeight.put("NON-CRIMINAL" ,1);
        categoryWeight.put("MINOR CRIMES" ,2);
        categoryWeight.put("SUSPICIOUS OCC" ,2);
        categoryWeight.put("OTHER OFFENSES" ,3);
        categoryWeight.put("VEHICLE THEFT", 4);
        categoryWeight.put("VANDALISM" ,5);
        categoryWeight.put("BURGLARY" ,6);
        categoryWeight.put("LARCENY/THEFT" ,7);
        categoryWeight.put("ASSAULT" ,8);
        categoryWeight.put("WARRANTS" ,8);

        return categoryWeight.get(category);

    }
    public int calcGridSize(double distance){

        int n=(int)(distance/DISTANCE_GAP_IN_METERS)+1;
        return n;
    }

    public double corrdinateGap(double Max,double min,int n){

        double cordianteGap=(Max-min)/n;
        return cordianteGap;
    }
    public double getdLat() {
        return dLat;
    }

    public void setdLat(double dLat) {
        this.dLat = dLat;
    }

    public double getdLon() {
        return dLon;
    }

    public void setdLon(double dLon) {
        this.dLon = dLon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
