package org.abithana.prediction;

import org.abithana.preprocessor.facade.PreprocessorFacade;
import org.abithana.utill.CrimeUtil;
import org.apache.spark.ml.feature.QuantileDiscretizer;
import org.apache.spark.ml.feature.StandardScaler;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.DataFrame;
import scala.Tuple2;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Thilina on 8/12/2016.
 * This class convert data into standard formats
 */
public class MLDataParser implements Serializable{

    //preprocessed dataframe.
    //must set methods to set dataframe after preprocess
    private PreprocessorFacade preprocessorFacade=new PreprocessorFacade();

    public DataFrame getFeaturesFrame(DataFrame df,String[] inputColumns,String outputColumnName){

        try {
            df=concatGISData(df);
            df=concatGISData(df);
            /*
            * for eg :
            *
            * VectorAssembler assembler2 = new VectorAssembler()
                .setInputCols(new String[]{"agencia_ID", "canal_ID", "ruta_SAK", "cliente_ID", "producto_ID"})
                .setOutputCol("features");
            * */


            df=indexingColumns(df,inputColumns);
            VectorAssembler vectorAssembler = new VectorAssembler()
                    .setInputCols(inputColumns)
                    .setOutputCol(outputColumnName);

            DataFrame featuredDF = vectorAssembler.transform(df);
            System.out.println("=======================================");
            System.out.println("Data frame Featuring Completed");
            System.out.println("=======================================");


            return featuredDF;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Convert all string type data columns in featured list into double valued columns
     */
    public DataFrame indexingColumns(DataFrame df,String[] featureCols){

        System.out.println("=================Indexing columns=================");

        String[] copyFeatrurecol=featureCols;

        Tuple2<String,String>[] colTypes=df.dtypes();

        for(int j=0;j<featureCols.length;j++) {
            for (int i = 0; i < colTypes.length; i++) {

                if(colTypes[i]._2().equals("StringType")) {
                    if (featureCols[j].equals(colTypes[i]._1())) {

                        StringIndexer indexer2 = new StringIndexer()
                                .setInputCol(copyFeatrurecol[j])
                                .setOutputCol(copyFeatrurecol[j]+"Index");
                        copyFeatrurecol[j]=copyFeatrurecol[j]+"Index";
                        df = indexer2.fit(df).transform(df);
                    }
                }
            }
        }

        System.out.println("============Indexing done!!!!!===============");


        return df;
    }

    /*
       * */
    public DataFrame preprocessTestData(DataFrame testSet){

        testSet=preprocessorFacade.handelMissingValues(testSet);

        List columns= Arrays.asList(testSet.columns());
        if(columns.contains("Dates")&&(!columns.contains("Time"))) {
            testSet=preprocessorFacade.getTimeIndexedTestDF(testSet, "Dates");
        }

        String[] dropColumns={"resolution","descript","address"};
        for(String s: dropColumns){
            testSet=preprocessorFacade.dropCol(testSet,s);
        }
        return testSet;
    }


    public String[] removeIndexWord(String[] array){
        for(int i=0;i<array.length;i++){
            if(array[i].contains("Index"))
                array[i]=array[i].substring(0,array[i].length()-5);
        }
        return array;
    }



    /*
    *  This method can use to Standardise the data set.
    *  input Col should be a Featured Dataframe
    *
    */
    public DataFrame standardiseData(DataFrame featuredDF, String inputCol, String outputCol){
        try {
            StandardScaler scaler = new StandardScaler()
                    .setInputCol(inputCol)
                    .setOutputCol(outputCol)
                    .setWithStd(true)
                    .setWithMean(true);

            DataFrame standardisedDf = scaler.fit(featuredDF).transform(featuredDF);

            return standardisedDf;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public DataFrame discritizeColoumn(DataFrame dataFrame,String inputCol,int noOfBuckets,String outputColName){
        try {
            QuantileDiscretizer discretizer = new QuantileDiscretizer()
                    .setInputCol(inputCol)
                    .setOutputCol(outputColName)
                    .setNumBuckets(noOfBuckets);
            return discretizer.fit(dataFrame).transform(dataFrame);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return dataFrame;
    }

    public DataFrame indexColumn(DataFrame df,String colName ,String indexedColName){

        try{
            CrimeUtil crimeUtil=new CrimeUtil();
            boolean colexists=crimeUtil.isColExists(df,colName);

            if(colexists){
                StringIndexer indexer = new StringIndexer()
                        .setInputCol(colName)
                        .setOutputCol(indexedColName);

                DataFrame indexed = indexer.fit(df).transform(df);
                return indexed;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return df;
    }

    public DataFrame concatGISData(DataFrame df){
        //TODO get GIS data and convert it to a vector and merge that vector col with df (here you must add GIS_data col name for String[] feature_columns in df).
        //then pass it to ML data paser adn get featuredDF and train the model
        return df;
    }
    public DataFrame concatWeatherDta(DataFrame df){
        //TODO get GIS data and convert it to a vector and merge that vector col with df (here you must add GIS_data col name for String[] feature_columns in df).
        //then pass it to ML data paser adn get featuredDF and train the model
        return df;
    }
}
