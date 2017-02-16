package org.abithana.prediction;

import org.abithana.utill.Config;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilina on 12/1/2016.
 */
public abstract class ClassificationModel implements Serializable{

    String generated_feature_col_name="features";
    String indexedLabel="indexedLabel";
    String indexedFeatures="indexedFeatures";
    String prediction ="prediction";
    String predictedLabel="predictedLabel";
    Config instance =Config.getInstance();
    String[] feature_columns;
    String[] testFeature_columns;
    String label;
    String modelType;
    List<Evaluation> evalList=new ArrayList<>();

    MLDataParser mlDataParser=new MLDataParser();

    Model model;

    abstract Pipeline getPipeline(DataFrame trainData);

    /*Method to get eveluation results of created model*/
    public List<Evaluation> getEvaluationResult(){
        return evalList;
    }

    /*
    * Use only getPipeline set to getPipeline model and get accuracy
    * */
    public void train_crossValidatorModel(DataFrame train, double partition,int folds)throws Exception{

        modelType="crossvalidation";

        /*transform trian set to features*/
        DataFrame trainData=getFeaturesFrame(train, feature_columns);

        Pipeline pipeline= getPipeline(trainData);
        if(partition>=1){
            partition=0.7;
        }

        DataFrame[] splits = trainData.randomSplit(new double[] {partition,(1-partition)});
        DataFrame trainingData = splits[0];
        DataFrame evalData = splits[1];

        ParamMap[] paramGrid = new ParamGridBuilder().build();

        // Run cross-validation, and choose the best set of parameters.
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol(indexedLabel)
                .setPredictionCol(prediction)
                        // "f1", "precision", "recall", "weightedPrecision", "weightedRecall"
                .setMetricName("precision");
        try {
            evaluator.write().overwrite().save("./models/evaluator");
            System.out.println("evaluator");
        }catch (Exception e){
            System.out.println("====================================");
            System.out.println("CANNOT SAVE evaluator");
            System.out.println("====================================");
            e.printStackTrace();
        }
        CrossValidator cv = new CrossValidator()
                .setEstimator(pipeline)
                .setEvaluator(evaluator)
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(folds);

        CrossValidatorModel model = cv.fit(trainingData);
        PipelineModel pipelineModel= (PipelineModel) model.bestModel();
        try {
            pipelineModel.write().overwrite().save("./models/bestPiplineModel");
            System.out.println("CrossValidator");
        }catch (Exception e){
            System.out.println("====================================");
            System.out.println("CANNOT SAVE CrossValidator");
            System.out.println("====================================");
            e.printStackTrace();
        }


        if(model!=null){
            modelType="crossvalidation";
            try {
                model.write().overwrite().save("./models/CrossValidationModel");
                model.write().overwrite().save("./models/lastSavedModel");
            }catch (Exception e){
                System.out.println("====================================");
                System.out.println("CANNOT SAVE CrossValidatorModel");
                System.out.println("====================================");
                e.printStackTrace();
            }

            DataFrame evaluations = model.transform(evalData);
            evaluationProcess(evaluations);
            /*CrossValidator cv = new CrossValidator()
                    .setEstimator(pipeline)
                    .setEvaluator(new MulticlassClassificationEvaluator())
                    .setEstimatorParamMaps(paramGrid)
                    .setNumFolds(10);*/

        }
        else {
            throw new Exception("no trained randomForest classifier model found in cross validation");
        }
    }

    public void train_pipelineModel(DataFrame train, double partition)throws Exception{

        /*transform trian set to features*/
        DataFrame trainData=getFeaturesFrame(train, feature_columns);
        /*preprocess user given test data set and transform it*/
      //  DataFrame testData=getTestFeatureFrame(test);

        Pipeline pipeline= getPipeline(trainData);

        if(partition>=1){
            partition=0.7;
        }

        DataFrame[] splits = trainData.randomSplit(new double[] {partition,(1-partition)});
        DataFrame trainingData = splits[0];
        DataFrame evalData = splits[1];

        PipelineModel model = pipeline.fit(trainingData);

        Transformer tr=model.stages()[2];


        if(model!=null){
            modelType="pipeline";
            try{
                model.write().overwrite().save("./models/pipelineModel");
                model.write().overwrite().save("./models/lastSavedModel");
            }catch (Exception e){
                System.out.println("====================================");
                System.out.println("CANNOT SAVE Pipeline method");
                System.out.println("====================================");
                e.printStackTrace();

            }

            DataFrame evaluations = model.transform(evalData);
            evaluationProcess(evaluations);

        }
        else {
            throw new Exception("no trained randomForest classifier model found in Pipeline Model");
        }
    }

    public DataFrame predict(DataFrame test,String[] feature_columns){
        try{
            test.show(30);
            Model model=null;
            DataFrame testData=test;
            this.testFeature_columns=feature_columns;
            this.feature_columns=feature_columns;
            test=mlDataParser.preprocessTestData(test);
            /*
            * if train and predict both happen at once
            * */
            if(modelType=="crossvalidation") {
                model = CrossValidatorModel.load("./models/CrossValidationModel");
                testData=getFeaturesFrame(test,mlDataParser.removeIndexWord(testFeature_columns));
            }
            else if(modelType=="pipeline"){
                model=PipelineModel.load("./models/pipelineModel");
                testData=getFeaturesFrame(test,mlDataParser.removeIndexWord(testFeature_columns));
            }
            else{
                //if predict for a previously saed model
                boolean modelfound=false;
                try{
                    model=PipelineModel.load("./models/lastSavedModel");
                    modelfound=true;
//                    testData=getFeaturesFrame(test,testFeature_columns);
                    testData=getTestFeatureFrame(test);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(!modelfound){
                    model=CrossValidatorModel.load("./models/lastSavedModel");
                    //testData=getFeaturesFrame(test,testFeature_columns);
                    testData=getTestFeatureFrame(test);
                    modelfound=true;
                }
            }
            vectorIndexerTest(testData);

            if(model!=null){

                DataFrame predictions_W = model.transform(testData);
                predictions_W.registerTempTable("prediction");

                String colAsString=getOutputColsAsString();

                predictions_W=Config.getInstance().getSqlContext().sql("select "+colAsString+" from prediction");
                predictions_W.show(40);

                if(savePrediction(predictions_W)){
                    instance.getSqlContext().dropTempTable("prediction");
                    instance.getSqlContext().dropTempTable("test");
                    return predictions_W;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public boolean savePrediction(DataFrame dataFrame){
        try {
            dataFrame
                    // place all data in a single partition
                    .coalesce(1)
                    .write().format("com.databricks.spark.csv")
                    .option("header", "true")
                    .mode("overwrite")
                    .save("./src/main/webapp/resources/predictionCsv");
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /*
    * Download prediction file written
    * */
    public String downloadFile() {
        try{
        String filePathString = "/resources/predictionCsv/part-00000";
        File f = new File(filePathString);
        if (f.exists() && !f.isDirectory()) {
            return filePathString;
        }
    }catch(Exception e){
        e.printStackTrace();
        }
        return "/resources/predictionCsv/part-00000";
    }
    /*
    * Evaluate the generated model
    * */
    private void evaluationProcess(DataFrame evaluations){

        try {
            evaluations.registerTempTable("predictions");

            DataFrame predictionAndLabels = evaluations.select("prediction", "indexedLabel");
            MulticlassMetrics metrics= new MulticlassMetrics(predictionAndLabels) ;

            Matrix confusion=metrics.confusionMatrix();

            DataFrame  evaluation= instance.getSqlContext().sql("select distinct category, avg(indexedLabel) as index  from predictions group by category");
            List<Row> lli=evaluation.collectAsList();

            for(Row r:lli){
                String cat=r.getString(0);
                double index=r.getDouble(1);
                System.out.println(r.getString(0)+"-"+r.getDouble(1));
                for(double i:metrics.labels()){

                    if(i==index){
                        Evaluation e=new Evaluation(i,cat,metrics.precision(i),metrics.recall(i),metrics.fMeasure(i));
                        evalList.add(e);
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * generate extra feature vector column to given dataset
     * */
    public DataFrame getFeaturesFrame(DataFrame df,String[] featureCols){

        if(generated_feature_col_name!=null){
            return new MLDataParser().getFeaturesFrame(df,featureCols, generated_feature_col_name);
        }
        else
            return null;
    }

    public DataFrame getTestFeatureFrame(DataFrame test){
        try{
            test=mlDataParser.preprocessTestData(test);
            DataFrame testData=getFeaturesFrame(test,mlDataParser.removeIndexWord(testFeature_columns));
            return testData;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getOutputColsAsString(){
        String cols[]=mlDataParser.removeIndexWord(feature_columns);
        String s=cols[0];

        for(int i=1;i<cols.length;i++){
            s=s+","+cols[i];
        }
        s=s+","+predictedLabel;
        return s;
    }

    public void vectorIndexerTest(DataFrame testData){
        VectorIndexerModel featureIndexerTest = new VectorIndexer()
                .setInputCol(generated_feature_col_name)
                .setOutputCol(indexedFeatures)
                .setMaxCategories(40)
                .fit(testData);
    }

    public static ClassificationModel getDefalutModel(){
        return new NaiveBaysianCrimeClassifier();
    }
}
