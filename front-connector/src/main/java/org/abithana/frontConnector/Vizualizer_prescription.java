package org.abithana.frontConnector;

import org.abithana.ds.CrimeDataStore;
import org.abithana.ds.PreprocessedCrimeDataStore;
import org.abithana.preprocessor.facade.PreprocessorFacade;
import org.abithana.prescription.impl.Redistricting.Cluster;
import org.abithana.prescription.impl.Redistricting.DistrictBoundryDefiner;
import org.abithana.prescription.impl.patrolBeats.*;
import org.abithana.statBeans.HistogramBeanDouble;
import org.apache.spark.sql.DataFrame;

import java.util.*;

/**
 * Created by Thilina on 1/22/2017.
 */
public class Vizualizer_prescription {

    private int state=0;
    PreprocessorFacade preprocessorFacade                   =new PreprocessorFacade();
    PreprocessedCrimeDataStore preprocessedCrimeDataStore   =PreprocessedCrimeDataStore.getInstance();
    CrimeDataStore initaldataStore                          =CrimeDataStore.getInstance();
    PrescriptionData prescriptionData                       =new PrescriptionData();
    TractCentroid t                                         =new TractCentroid();
    DistrictBoundryDefiner districtBoundryDefiner;
    PatrolBoundry p=new PatrolBoundry();
    /*
    this method runs if there is no preprocessing done previously
    */
    private void doPreprocessing(){
        /*get initial dataFrame and preprocess it only for prescription*/
        DataFrame df=preprocessorFacade.handelMissingValues(initaldataStore.getDataFrame());
        DataFrame f2=preprocessorFacade.handelMissingValues(df);

        List columns= Arrays.asList(f2.columns());
        if(columns.contains("dateAndTime")&&(!columns.contains("Time"))) {
            f2=preprocessorFacade.getTimeIndexedDF(f2, "dateAndTime");
        }
        System.out.println("==================================================================================");
        System.out.println("                            PREPROCESSED DATA");
        System.out.println("==================================================================================");
        f2.show(30);
        preprocessedCrimeDataStore.saveTable(f2, "preprocessedData");
    }

    /*
    * get police redistrictring results
    * */
    public HashMap<Long,Cluster> getRedistrictBoundry(long numberOfDistricts, long populationTotal){
        districtBoundryDefiner=new DistrictBoundryDefiner(numberOfDistricts,populationTotal);
        return districtBoundryDefiner.redrawingDistrictBoundry();
    }

    public Map<Long,ClusterPatrol> generatePatrolBeats(long districtID,int patrolBeats,int season,int weekdays,int watch){

        if(preprocessedCrimeDataStore.getDataFrame()==null) {
            doPreprocessing();
        }

        String tblname=preprocessedCrimeDataStore.getTableName();
        prescriptionData.setCategory("category");
        prescriptionData.setLat("latitude");
        prescriptionData.setLon("longitude");
        String query=prescriptionData.patrolQueryGenerator(tblname, weekdays, watch,season);
        String prescriptionTblName="prescription";

        if(districtBoundryDefiner!=null) {
            prescriptionData.createPrescriptionDs(districtBoundryDefiner, districtID, prescriptionTblName, query);
            state=10;
            Checker ch = new Checker();
            p.getLearders( t.getAllBlockCentroids(prescriptionTblName),patrolBeats);
            state=20;
            p.calcThreashold( t.getToalWork(),patrolBeats);
            state=50;
            p.findPatrolBoundries();
            state=80;
            return ch.convertToCluster(p.getBoundryTractids());
        }
        else {
            return null;
        }

       // prescriptionData.digitizeMap(tblname,prescriptionTblName);

    }

    public List<HistogramBeanDouble> evaluateResponseTime(){
        HashMap<Integer,Double> evaluate=p.evaluateBeatsResposeime();

        List<HistogramBeanDouble> list=new ArrayList();
        for(int i:evaluate.keySet()){
            list.add(new HistogramBeanDouble(""+i,evaluate.get(i)));
            System.out.println("Beats ID : " + i + " 911 response time :" + evaluate.get(i));
        }

        return list;
    }

    public List<HistogramBeanDouble> evaluateBeatsWorkload(){
        HashMap<Integer,Integer> evaluateWorkload=p.evaluateBeatsWorkload();

        List<HistogramBeanDouble> list=new ArrayList();
        for(int i:evaluateWorkload.keySet()){
            list.add(new HistogramBeanDouble(""+i,evaluateWorkload.get(i)));
        }

        return list;

    }

    public List<HistogramBeanDouble> evaluateBeatsCompactness(){
        HashMap<Integer,Double> evaluate=p.evaluateBeatsCompactness();

        List<HistogramBeanDouble> list=new ArrayList();
        for(int i:evaluate.keySet()){
            list.add(new HistogramBeanDouble(""+i,evaluate.get(i)));
        }

        return list;

    }

    /*
    * State value represents how far computation has done
    * state=20 represents 20% computations done
    * state=80 represents 80% computations done
    *
    * */
    public int getState(){
        return state;
    }
}
