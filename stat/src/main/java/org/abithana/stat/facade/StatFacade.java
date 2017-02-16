package org.abithana.stat.facade;

import org.abithana.stat.support.DataSummary;
import org.abithana.statBeans.CordinateBean;
import org.abithana.statBeans.HistogramBean;
import org.abithana.utill.Config;
import org.abithana.utill.Converter;
import org.abithana.utill.CrimeUtil;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 11/19/2016.
 */
public class StatFacade implements Serializable {

    private Config instance=Config.getInstance();

    public String queryProcessor(String query){
        return null;
    }

    public List<HistogramBean> getVisualizeList(DataFrame df){

        List<HistogramBean> list=new ArrayList<>();
        for(Row rw:df.collect()){
            HistogramBean histogramBean = new HistogramBean(""+rw.get(0),rw.getLong(1));
            list.add(histogramBean);
        }
        return list;

    }

    /*
    * Crime frequncy of group by days of Week
    * */
    public DataFrame getDayWiseFrequncy(String tblName,DataFrame df,int year,int month){

        String query="Select dayOfWeek,count(*) from "+tblName+" where year="+year+" and month="+month+" group by dayOfWeek ";
       // String query="Select dayOfWeek,count(*) from "+tblName+" where year="+year+"  group by dayOfWeek ";
        return queryTimeIndexDf(df,query,tblName);
    }


    /*
    * Crime frequncy of group by days of Week
    * */
    public DataFrame getDayWiseFrequncy_forRange(String tblName,DataFrame df,int yf,int mf,int yt,int mt){

        String query="Select dayOfWeek,count(*) from "+tblName+" where year >="+yf+" and year<="+yt+" and month >= "+mf+" and month <= "+mt+" group by dayOfWeek ";
        return queryTimeIndexDf(df,query,tblName);
    }

    /*
    * Crime heatmap for date range
    * */
    public DataFrame getHeatmap_forRange(String tblName,DataFrame df,int yf,int mf,int dayf,int yt,int mt,int dayt){

        String query="Select latitude,longitude from "+tblName+" where year >="+yf+" and year<="+yt+" and month >= "+mf+" and month <= "+mt+" and day>="+dayf+" and day<="+dayt ;
        return queryTimeIndexDf(df,query,tblName);
    }

    /*
    * Crime heatmap for given date
    * */
    public DataFrame getspecifcdayHeatmap(String tblName,DataFrame df,int y,int m,int d){

        String query="Select latitude,longitude from "+tblName+" where year ="+y+" and  month = "+m+"  and day="+d ;
        return queryTimeIndexDf(df,query,tblName);
    }

    public List<CordinateBean> getCordinateList(DataFrame df){

        List<CordinateBean> list=new ArrayList<>();
        for(Row rw:df.collect()){

            CordinateBean cordinateBean = new CordinateBean(rw.getAs("latitude"),rw.getAs("longitude"));
            list.add(cordinateBean);
        }
        return list;
    }

    /*get data crime categorywise
    * this gives data to implement categorywise heat map
    * */
    public DataFrame categoryWiseData(DataFrame df,String[] categories){

        try {


           // crimeUtil=new CrimeUtil();
          //  df=crimeUtil.getTimeIndexedDF(df, "Dates");

            String s = " ";
            for (int i = 0; i < categories.length; i++) {
                if (i == 0)
                    s = s + " where ";

                s = s + "category=" + "'" + categories[i] + "'";
                if (i + 1 != categories.length)
                    s = s + " or ";
            }

            String tblName="DataTbl";
            String query="Select * from "+tblName+" " + s;
            return queryTimeIndexDf(df,query,tblName);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public DataFrame categoryWiseCoordinates(DataFrame df,String[] categories){

        try {
            // crimeUtil=new CrimeUtil();
            //  df=crimeUtil.getTimeIndexedDF(df, "Dates");

            String s = " ";
            for (int i = 0; i < categories.length; i++) {
                if (i == 0)
                    s = s + " where ";

                s = s + "category=" + "'" + categories[i] + "'";
                if (i + 1 != categories.length)
                    s = s + " or ";
            }

            String tblName="DataTbl";
            String query="Select latitude,longitude from "+tblName+" " + s ;
            return queryTimeIndexDf(df,query,tblName);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    /*get data crime categorywise
       * this gives data to implement categorywise heat map
       * */
    public DataFrame categoryWiseColData(DataFrame df,String cat,String col){

        String tblName="DataTbl";
        String query="Select " + col + " from "+tblName+" where category=" + "'" + cat + "'" ;
        return queryTimeIndexDf(df,query,tblName);
    }

    /*get data crime year wise
       * this gives data to visualize histogram
       * */
    public DataFrame categoryTimeData(DataFrame df,String cat){

        String tblName="DataTbl";
        String query="Select year,count(*) from "+tblName+" where category='" + cat + "' group by year " ;
        return queryTimeIndexDf(df,query,tblName);
    }

    /*get data crime categorywise
   * this gives data to visualize histogram
   * */
    public DataFrame yearCategoryData(DataFrame df,int year){

        String tblName="DataTbl";
        String query="Select category,count(*) from "+tblName+" where year='"+year+"' group by category ";
        return queryTimeIndexDf(df,query,tblName);
    }

    /*get data crime categorywise frequncy according to given year range
    * this gives data to visualize histogram
    * */
    public DataFrame categoryFrequency_givenTimeRange(DataFrame df, int yearFrom, int yearTo){

        String tblName="DataTbl";
        String query="Select latitude,longitude from "+tblName+" where year between '" + yearFrom + "' AND '" + yearTo + "' ";
        return queryTimeIndexDf(df,query,tblName);
    }

    /*get data crime categorywise frequncy according to given year range
    * this gives data to visualize histogram
    * */
    public DataFrame crimesInYear(DataFrame df, int year){

        String tblName="DataTbl";
        String query="Select latitude,longitude from "+tblName+" where year ="+year;
        return queryTimeIndexDf(df,query,tblName);
    }

    public DataFrame getYears(DataFrame df){
        String tblName="DataTbl";
        String query="Select distinct year from "+tblName;
        return queryTimeIndexDf(df,query,tblName);

    }

    public DataFrame timeWiseData(DataFrame df,int timeFrom,int timeTo){

        //convert Date to 1-24 time hours
        String tblName="DataTbl";
        String query="Select * from "+tblName+" where time between '" + timeFrom + "' AND '" + timeTo + "'";
        return queryTimeIndexDf(df,query,tblName);

    }
    public DataFrame weekDaysCrime(DataFrame df){

        String tblName="DataTbl";
        String query="Select latitude,longitude from " + tblName + " where dayOfWeek not LIKE 'SAT%' and dayOfWeek not like 'SUN%' ";
        return queryTimeIndexDf(df,query,tblName);

    }

    public DataFrame weekEndsCrime(DataFrame df){

        String tblName="DataTbl";
        String query="Select latitude,longitude from "+tblName+" where dayOfWeek LIKE 'Sun%' or dayOfWeek like 'Sat%' ";
        return queryTimeIndexDf(df,query,tblName);
    }

    public DataFrame newYearEveCrime(DataFrame df){

        String tblName="DataTbl";
        String query="Select latitude,longitude from "+tblName+" where month = 12 and day=31 and time > 18 ";
        return queryTimeIndexDf(df,query,tblName);

    }

    public DataFrame queryTimeIndexDf(DataFrame df,String query,String tblName){

        //convert Date to 1-24 time hours
       /* CrimeUtil cu=new CrimeUtil();
        df=cu.getTimeIndexedDF(df, "Dates");
*/
        try {
            df.registerTempTable(tblName);
            DataFrame dataFrame = instance.getSqlContext().sql(query);
            instance.getSqlContext().dropTempTable(tblName);
            return dataFrame;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public List getAllFields(DataFrame dataFrame) {
        String str = dataFrame.collectAsList().toString();
        String[] FnTs = str.substring(1,str.length()-1).split(",");
        List<String> fields = new ArrayList();
        for(String s : FnTs){
            fields.add(s.split(":")[1].trim().toString());
        }
        return fields;
    }

    public List getSubFields(DataFrame dataFrame, String baseField) {
        String str = dataFrame.collectAsList().toString();
        String[] FnTs = str.substring(1,str.length()-1).split(",");
        List<String> fields = new ArrayList();
        for(String s : FnTs){
            fields.add(s.split(":")[1].trim().toString());
        }
        fields.remove((Object)baseField);
        return fields;
    }

    public DataSummary getSummary(DataFrame dataFrame,String baseField,String baseClass){
        DataSummary dataSummary = new DataSummary(baseField);
        dataSummary.setRecords(summarize(dataFrame,baseField,baseClass));
        return dataSummary;
    }

    private List<ArrayList> summarize(DataFrame dataFrame,String baseField,String baseClass){;


        List<String> subFields = new ArrayList<String>();

        dataFrame.registerTempTable("dataset");
        dataFrame.show(30);
        StringBuilder stringBuilder = new StringBuilder();
        for(String field : subFields){
            stringBuilder.append(field+",");
        }

        if(stringBuilder.length()>0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        String sqlQuery = "SELECT " + stringBuilder.toString() + " FROM dataset " +
                " WHERE " + baseField + " = " + "'"+baseClass+"'";

        List<ArrayList> list;
        DataFrame df = instance.getSqlContext().sql(sqlQuery);
        Converter converter = new Converter();
        list = converter.convert(df);
        instance.getSqlContext().dropTempTable("dataset");
        return list;
    }
}
