package org.abithana.preprocessor.impl;

import org.apache.spark.ml.feature.QuantileDiscretizer;
import org.apache.spark.sql.DataFrame;

/**
 * Created by acer on 11/23/2016.
 */
public class Discretizer extends Preprocessing {

    public DataFrame discretizeColumn(DataFrame f1,String columnname,int partitionSize){
        try {
            String st="distimeIndex";
            QuantileDiscretizer discretizer = new QuantileDiscretizer()
                    .setInputCol(columnname)
                    .setOutputCol(st)
                    .setNumBuckets(partitionSize);
            for(String s:f1.columns()){
                System.out.println(s);
            }
           f1=discretizer.fit(f1).transform(f1);
            f1.show(5);
            dropCol(f1, columnname);
            return f1.withColumnRenamed("dis"+columnname,columnname);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return f1;
    }

    DataFrame partitionToEqualDepth(DataFrame f1,String columnname,int partitionSize){
        return null;

    }

    DataFrame partitionTimeRange(){
        return null;

    }

    DataFrame createLatLonGrid(){
        return null;

    }

    DataFrame partitionIntoSeasons(DataFrame f1,String yearCol){
        return null;

    }
}
