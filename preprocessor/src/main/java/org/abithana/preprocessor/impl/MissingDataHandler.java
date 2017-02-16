package org.abithana.preprocessor.impl;


import org.apache.spark.sql.DataFrame;

/**
 * Created by acer on 11/16/2016.
 */
public class MissingDataHandler extends Preprocessing {

    void getNoOfRows(DataFrame df){
        df.count();
    }

    public DataFrame deleteRows(DataFrame df){
        try {

            df=df.na().drop();
        }catch (Exception e){

        }
        return df;

    }

    DataFrame replaceByFrequentItem(DataFrame df){


        return df;

    }

    DataFrame addnewValue(DataFrame df,String col,String value){
        return df;

    }

    DataFrame predictAndFill(DataFrame df){
        return df;

    }
}
