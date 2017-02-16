package org.abithana.utill;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

import java.io.Serializable;

/**
 * Created by acer on 11/19/2016.
 */
public class Config implements Serializable{

    private final transient SparkConf conf = new SparkConf().setMaster("local").setAppName("DataReader");
    private final transient JavaSparkContext sc  = new JavaSparkContext(conf);
    private final transient SQLContext sqlContext =  new org.apache.spark.sql.SQLContext(sc);

    private static Config instance=new Config();

    private Config(){

    }

    public static Config getInstance(){
        if(instance==null)
            instance=new Config();
        return instance;
    }

    public SparkConf getConf() {
        return conf;
    }

    public JavaSparkContext getSc() {
        return sc;
    }

    public SQLContext getSqlContext() {
        return sqlContext;
    }
}
