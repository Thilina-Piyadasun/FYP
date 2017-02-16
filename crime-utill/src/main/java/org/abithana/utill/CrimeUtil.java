package org.abithana.utill;

import org.abithana.beans.CrimeDataBeanWithTime;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Thilina on 8/27/2016.
 */
public class CrimeUtil implements Serializable {

    Config instance=Config.getInstance();

    public boolean isColExists(DataFrame df, String colName)throws Exception{

        String[] columns=df.columns();

        int count=0; //for check whether  duplicate columns exists or not
        boolean colExists=false;
       /* for(String s:df.columns()){
            if(s==colName)
                return true;
        }*/
        for(int i=0;i<columns.length;i++){
            if(colName.equals(columns[i])){
                colExists=true;
                count++;
            }
        }
        if(colExists){
            if(count==1)
                return true;
            else
                throw new Exception("Duplicate column names found " + colName);
        }
        else
            throw new Exception("column not found " + colName);

    }




}

