package org.abithana.prescription.facade;

import org.abithana.prescription.impl.patrolBeats.PrescriptionData;

/**
 * Created by Thilina on 1/23/2017.
 */
public class PrescriptionFacade {

    PrescriptionData prescriptionData=new PrescriptionData();
    /*
    * This generate patrol beats boundry without any restrictions
    * */
    public void generateOverallpatrolBeatsBoundary(String name,String datasetUsing){
      //  prescriptionData.createPrescriptionDs(name,datasetUsing,"");
    }

    /*
    *  First Watch : 0300-1200 Morning shift -shift id 1
       Second Watch: 1100-2000 evening shift -shift id 2
       Third Watch: 1900-0400  night shift -shiftid 3
    *  overall shift id 0
    * */
    public void generateWeekdayBeatsBoundary(String name,String datasetUsing){

        //prescriptionData.createPrescriptionDs(name,datasetUsing,"");
    }

    /*
    *  First Watch : 0300-1200 Morning shift -shift id 1
       Second Watch: 1100-2000 evening shift -shift id 2
       Third Watch: 1900-0400  night shift -shiftid 3
    *  overall shift id 0
    * */
    public void generateWeekendsBeatsBoundary(){

    }
    /*
    *  First Watch : 0300-1200 Morning shift -shift id 1
       Second Watch: 1100-2000 evening shift -shift id 2
       Third Watch: 1900-0400  night shift -shiftid 3
    *  overall shift id 0
    * */
    public void generateSeasonsBeatsBoundary(){

    }


}
