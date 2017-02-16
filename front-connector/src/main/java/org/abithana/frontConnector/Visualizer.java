package org.abithana.frontConnector;

import org.abithana.ds.*;
import org.abithana.prediction.*;
import org.abithana.preprocessor.facade.PreprocessorFacade;
import org.abithana.stat.facade.StatFacade;
import org.abithana.statBeans.CordinateBean;
import org.abithana.statBeans.HistogramBean;
import org.abithana.utill.Config;
import org.abithana.utill.Converter;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by acer on 11/23/2016.
 */
public class Visualizer  implements Serializable{

    private int state=0;
    private String[] dropColumns={"resolution","descript","address"};

    PreprocessorFacade preprocessorFacade=new PreprocessorFacade();
    CrimeDataStore initaldataStore =CrimeDataStore.getInstance();
    DataStore preProcesedDataStore= PreprocessedCrimeDataStore.getInstance();
    Converter converter=new Converter();
    PopulationDataStore populationDataStore=PopulationDataStore.getInstance();

    public void doPreprocessing(String prepTableName){

        DataFrame df= initaldataStore.getDataFrame();
        state=1;
        DataFrame f2=preprocessorFacade.handelMissingValues(df);
        state=2;
        List columns= Arrays.asList(f2.columns());
        if(columns.contains("dateAndTime")&&(!columns.contains("Time"))) {
            f2=preprocessorFacade.getTimeIndexedDF(f2, "dateAndTime");
        }
        state=3;
        for(String s: dropColumns){
            f2=preprocessorFacade.dropCol(f2,s);
        }
        f2.show(3);
        /*At final step in preprocessing save data frame in PreprocessedDataStore*/
        preProcesedDataStore.saveTable(f2,prepTableName);
        if(populationDataStore.getDataFrame()!=null) {
            f2 = preprocessorFacade.integratePopulationData(populationDataStore.getTableName(), prepTableName);
            state=4;
        }


        preProcesedDataStore.saveTable(f2,prepTableName);
        state=5;
        preProcesedDataStore.getDataFrame().show(40);
    }

    public int getState(){
        return state;
    }

    /*
    * to execute queries from visualization
    * */
    public ArrayList<ArrayList> executeQueries(String query){
        try {
            DataFrame dataFrame= initaldataStore.queryDataSet(query);
            ArrayList<ArrayList> list=converter.convert(dataFrame);
            return list;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*
    for a given category retruns the freuquency of respective crime category in each year
    eg:
    2001 34
    2002 505
    2003 56
    * */
    public List<HistogramBean> categoryWiseData(String category){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.categoryTimeData(df, category);
        return statFacade.getVisualizeList(dataFrame);
    }

    public List<HistogramBean> getHeatmap_forRange(int yearFrom,int mothFrom,int dayFrom,int yearTo,int monthTo,int DayTo){
        DataFrame df= preProcesedDataStore.getDataFrame();
        String table=preProcesedDataStore.getTableName();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.getHeatmap_forRange(table,df,  yearFrom, mothFrom, dayFrom, yearTo, monthTo, DayTo);
        return statFacade.getVisualizeList(dataFrame);
    }

    public List<HistogramBean> getHeatmap_forRange(int year,int moth,int day){
        DataFrame df= preProcesedDataStore.getDataFrame();
        String table=preProcesedDataStore.getTableName();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.getspecifcdayHeatmap(table,df,  year, moth, day);
        return statFacade.getVisualizeList(dataFrame);
    }

    /*
    * Crime frequency according to day
    * Monday 2344
    * Tuesday 4545
    * etc.
    * */
    public List<HistogramBean> getDayWiseFrequncy(int year,int month){
        DataFrame df= preProcesedDataStore.getDataFrame();
        String table=preProcesedDataStore.getTableName();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.getDayWiseFrequncy(table,df,year,month);
        List<HistogramBean> list= statFacade.getVisualizeList(dataFrame);
        if(list.size()==7)
            return list;
        else if(list.size()<7){
            List<String> days=new ArrayList<>();
            days.add("Monday");days.add("Tuesday");days.add("Wednesday");days.add("Thursday");days.add("Saturday");days.add("Sunday");

            for(HistogramBean hb:list){
                if(days.contains(hb.getLabel())){
                    days.remove(hb.getLabel());
                }
            }

            for(String s:days){
                list.add(new HistogramBean(s,0));
            }
        }
        return list;
    }

    /*
    * Crime frequency according to day
    * Monday 2344
    * Tuesday 4545
    * etc.
    * */
    public List<HistogramBean> getDayWiseFrequncy_forRange(int yearfrom,int monthfrom,int yearto,int monthto){
        DataFrame df= preProcesedDataStore.getDataFrame();
        String table=preProcesedDataStore.getTableName();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.getDayWiseFrequncy_forRange(table,df,yearfrom,monthfrom,yearto,monthto);
        dataFrame.show(50);
        List<HistogramBean> list= statFacade.getVisualizeList(dataFrame);
        if(list.size()==7)
            return list;
        else if(list.size()<7){
            List<String> days=new ArrayList<>();
            days.add("Monday");days.add("Tuesday");days.add("Wednesday");days.add("Thursday");days.add("Saturday");days.add("Sunday");

            for(HistogramBean hb:list){
                if(days.contains(hb.getLabel())){
                    days.remove(hb.getLabel());
                }
            }

            for(String s:days){
                list.add(new HistogramBean(s,0));
            }
        }
        return list;

    }

    /*
    * Data for heat ap visualization
    * */
    public List<CordinateBean> heatMapData(String[] categories){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.categoryWiseCoordinates(df,categories);
        return statFacade.getCordinateList(dataFrame);
    }

    /*
    * for a given year freaquncy of each caegory
    * */
    public List<HistogramBean> yearWiseData(int year){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.yearCategoryData(df, year);
        return statFacade.getVisualizeList(dataFrame);
    }

    /*
    * Data for time line animation
    * */
    public List<CordinateBean> timeLineAnimation(int startYear,int endYear){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.categoryFrequency_givenTimeRange(df,startYear,endYear);
        return statFacade.getCordinateList(dataFrame);

    }

    /*
    * Data year wise
    * */
    public List<CordinateBean> crimesInyear(int year){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.crimesInYear(df,year);
        return statFacade.getCordinateList(dataFrame);

    }

    public List<Integer> getYears(){

        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        List<Row> list=statFacade.getYears(df).collectAsList();
        List<Integer> list1=new ArrayList<>();
        for(Row r:list){
            list1.add(r.getAs("year"));
        }
        return list1;
    }

    public List<CordinateBean> weekDaysCrimeLoc(){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.weekDaysCrime(df);
        return statFacade.getCordinateList(dataFrame);
    }

    public List<CordinateBean> weekendsCrimeLoc(){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.weekEndsCrime(df);
        return statFacade.getCordinateList(dataFrame);
    }

    public List<CordinateBean> newYearEveCrimes(){
        DataFrame df= preProcesedDataStore.getDataFrame();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.newYearEveCrime(df);
        return statFacade.getCordinateList(dataFrame);
    }
    /*
    * Categories of data set after preprocessing
    * */
    public List<String> getCategories(){

        try {
            String prepTableName=preProcesedDataStore.getTableName();
            DataFrame dataFrame= preProcesedDataStore.queryDataSet("Select distinct category from "+prepTableName);
            Converter converter=new Converter();
            List<Row> list=dataFrame.collectAsList();
            List<String> stringList=new ArrayList<>();
            for(Row row :list){
                stringList.add(row.getAs("category"));
            }
            return stringList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public long getNoofRows(){
        if(preProcesedDataStore.getDataFrame()==null){
            return 0;
        }
        return preProcesedDataStore.getDataFrame().count();
    }

    public String getPreproceesedTableName(){
        if(preProcesedDataStore.getDataFrame()==null){
            return "";
        }
        return preProcesedDataStore.getTableName();
    }

/*
    *//*
    * Prediction Methods
    * *//*
    public ArrayList<ArrayList> perceptronCrossVaidationModel(String testPath,String[] feature_columns, String label,int[] layers,int blockSize,long seed,int maxIterations,double partition,int folds){
        try {
            classificationModel=new MultilayerPerceptronCrimeClassifier(feature_columns,label,layers,blockSize,seed,maxIterations);
            DataFrame df=classificationModel.train_crossValidatorModel(preProcesedDataStore.getDataFrame(), initaldataStore.readCsv(testPath), partition, folds);

            ArrayList<ArrayList> list=converter.convert(df);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<ArrayList> RForestCrossVaidationModel(String testPath,String[] feature_columns, String label,int trees,int seed,double partition,int folds){
        try {
            classificationModel=new RandomForestCrimeClassifier(feature_columns,label,trees,seed);
            DataFrame df=classificationModel.train_crossValidatorModel(preProcesedDataStore.getDataFrame(), initaldataStore.readCsv(testPath), partition,folds);

            ArrayList<ArrayList> list=converter.convert(df);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<ArrayList> NBCrossVaidationModel(String testPath,String[] feature_columns, String label,double partition,int folds){
        try {
            classificationModel=new NaiveBaysianCrimeClassifier(feature_columns,label);
            DataFrame df=classificationModel.train_crossValidatorModel(preProcesedDataStore.getDataFrame(), initaldataStore.readCsv(testPath), partition,folds);

            ArrayList<ArrayList> list=converter.convert(df);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<ArrayList> perceptronPipelineModel(String testPath,String[] feature_columns, String label,int[] layers,int blockSize,long seed,int maxIterations,double partition){
        try {
            classificationModel=new MultilayerPerceptronCrimeClassifier(feature_columns,label,layers,blockSize,seed,maxIterations);
            DataFrame df=classificationModel.train_pipelineModel(preProcesedDataStore.getDataFrame(), initaldataStore.readCsv(testPath), partition);

            ArrayList<ArrayList> list=converter.convert(df);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ArrayList> RForestPipelineModel(String testPath,String[] feature_columns, String label,int trees,int seed,double partition){
        try {
            classificationModel=new RandomForestCrimeClassifier(feature_columns,label,trees,seed);
            DataFrame df=classificationModel.train_pipelineModel(preProcesedDataStore.getDataFrame(), initaldataStore.readCsv(testPath), partition);

            ArrayList<ArrayList> list=converter.convert(df);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<ArrayList> NBPipelineModel(String testPath,String[] feature_columns, String label,double partition){
        try {
            classificationModel=new NaiveBaysianCrimeClassifier(feature_columns,label);
            DataFrame df=classificationModel.train_pipelineModel(preProcesedDataStore.getDataFrame(), initaldataStore.readCsv(testPath), partition);

            ArrayList<ArrayList> list=converter.convert(df);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/

    public void setDropColumns(String[] dropColumns) {
        this.dropColumns = dropColumns;
    }
}
