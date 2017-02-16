package org.abithana.stat.statAnalyser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.abithana.stat.support.frequentCrimePatterns;
import org.abithana.stat.support.frequentCrimeSet;
import org.abithana.utill.Config;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

/**
 * Created by acer on 11/20/2016.
 */
public class Statistics {

    private Config instance=Config.getInstance();

    public void correlation(DataFrame df,String col1,String col2){

        df.stat().crosstab(col1,col2);
    }

    public DataFrame describeColumn(DataFrame df,String col){
        return df.describe(col);
    }

    public DataFrame describeColumns(DataFrame df,String[] cols){
        return df.describe(cols);
    }

    public DataFrame mostFrequentItems(DataFrame df, String[] col,double cutoff){
        DataFrame freq=df.stat().freqItems(col,cutoff);
        return freq;
    }

    public List<frequentCrimeSet> frequentItemSets(DataFrame df,double minSupport){

        List<frequentCrimeSet> frequentCrimeSets =new ArrayList<>();
        //get <List<String>> type RDD
        JavaRDD<List<String>> transactions =getTempRDDForFPGrowth(df);

        FPGrowth fpg = new FPGrowth()
                .setMinSupport(minSupport)
                .setNumPartitions(4);
        FPGrowthModel<String> model = fpg.run(transactions);

        for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
            frequentCrimeSet record=new frequentCrimeSet(itemset.javaItems(),itemset.freq());

            frequentCrimeSets.add(record);
        }
        return frequentCrimeSets;
    }

    public List<frequentCrimePatterns> mineFrequentPatterns(DataFrame df ,double minSupport,double conf) {

        List<frequentCrimePatterns> frequentCrimeRules =new ArrayList<>();
        //get <List<String>> type RDD
        JavaRDD<List<String>> transactions =getTempRDDForFPGrowth(df);

        FPGrowth fpg = new FPGrowth()
                .setMinSupport(minSupport)
                .setNumPartitions(4);
        FPGrowthModel<String> model = fpg.run(transactions);

        for (AssociationRules.Rule<String> rule : model.generateAssociationRules(conf).toJavaRDD().collect()) {
            System.out.println(
                    rule.javaAntecedent() + " => " + rule.javaConsequent() + ", " + rule.confidence());

            frequentCrimePatterns rules=new frequentCrimePatterns(rule.javaAntecedent(),rule.javaConsequent(),rule.confidence());
            frequentCrimeRules.add(rules);
        }
        return frequentCrimeRules;
    }

    private JavaRDD<List<String>> getTempRDDForFPGrowth(DataFrame df) {
        //TODO:Exception handel
        //TODO:

        df.registerTempTable("freqtable");
        df.show(40);

        //TODO: add time col after preporessor done
        DataFrame selected=instance.getSqlContext().sql("select category,dayOfWeek,pdDistrict,x,y,time from freqtable");

        JavaRDD<List<String>> transactions = selected.javaRDD().map(new Function<Row, List<String>>() {
            public List<String> call(Row row) {
                String[] items = {row.getString(0), row.getString(1),row.getString(2),""+ row.getDouble(3),""+row.getDouble(4)+""+row.getInt(5)};
                return Arrays.asList(items);
            }
        });

        return transactions;
    }
}
