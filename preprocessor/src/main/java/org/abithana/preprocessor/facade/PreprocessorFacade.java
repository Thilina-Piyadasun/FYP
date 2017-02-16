package org.abithana.preprocessor.facade;

import org.abithana.preprocessor.impl.Discretizer;
import org.abithana.preprocessor.impl.MissingDataHandler;
import org.abithana.preprocessor.impl.Preprocessing;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;

/**
 * Created by acer on 11/20/2016.
 */
public class PreprocessorFacade {

    MissingDataHandler missingDataHandler=new MissingDataHandler();
    Preprocessing preprocessing=new Preprocessing();
    Discretizer discretizer=new Discretizer();


    public DataFrame handelMissingValues(DataFrame df){
        System.out.println("Running missing value handling");
        return missingDataHandler.deleteRows(df);
    }

    public DataFrame getTimeIndexedDF(DataFrame df,String columnWithTime){
        System.out.println("Time and Date separation");
        return preprocessing.getTimeIndexedDF(df,columnWithTime);
    }

    public DataFrame getTimeIndexedTestDF(DataFrame df,String columnWithTime){
        System.out.println("Time and Date separation IN TEST");
        return preprocessing.getTimeIndexedDFforTest(df,columnWithTime);
    }

    public DataFrame discretizeColumn(DataFrame f1,String columnname,int partitionSize){
        System.out.println("Data disrcitization of column : "+ columnname);
        return discretizer.discretizeColumn(f1,columnname,partitionSize);
    }
    public DataFrame dropCol(DataFrame df,String column){

        return preprocessing.dropCol(df,column);
    }

    public DataFrame integratePopulationData(String populatinTableName,String preprocessTblName){
        return preprocessing.integratePopulationData(populatinTableName,preprocessTblName);
    }

    public Column getCol(DataFrame df,String column){

        return preprocessing.getCol(df,column);

    }
    DataFrame aggregateDataFrames(DataFrame f1,DataFrame f2){
        return preprocessing.aggregateDataFrames(f1,f2);
    }

    DataFrame stringIndexing(DataFrame df){
        return preprocessing.stringIndexing(df);
    }

    DataFrame createFeatureFrame(DataFrame f1){

        return preprocessing.createFeatureFrame(f1);

    }

    DataFrame removeDupliates(DataFrame f1){
        //TODO implement
        return preprocessing.removeDupliates(f1);

    }

    public String[][] getFetureSet() {

        return preprocessing.getFetureSet();
    }

    public void setFetureSet(String[][] fetureSet) {

        preprocessing.setFetureSet(fetureSet);
    }

    public int getRowLimit() {

        return preprocessing.getRowLimit();
    }

    public void setRowLimit(int rowLimit) {

        preprocessing.setRowLimit(rowLimit);
    }


}
