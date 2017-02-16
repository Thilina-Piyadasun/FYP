package org.abithana.prediction;


import org.abithana.utill.Config;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilina on 8/13/2016.
 */
public class RandomForestCrimeClassifier extends ClassificationModel {

    private int noOfTrees;
    private int seed;

    public RandomForestCrimeClassifier(String[] feature_columns, String label,int noOfTrees,int seed) {
        this.testFeature_columns = feature_columns;
        this.feature_columns=feature_columns;
        this.label = label;
        this.noOfTrees=noOfTrees;
        this.seed=seed;
    }


    Pipeline getPipeline(DataFrame trainData){
        try{

            StringIndexerModel labelIndexer = new StringIndexer()
                    .setInputCol(label)
                    .setOutputCol(indexedLabel)
                    .fit(trainData);
            labelIndexer.write().overwrite().save("models\\label");
            System.out.println("label indexer saved");
            // Automatically identify categorical features, and index them.
            // Set maxCategories so features with > 4 distinct values are treated as continuous.
            VectorIndexerModel featureIndexer = new VectorIndexer()
                    .setInputCol(generated_feature_col_name)
                    .setOutputCol(indexedFeatures)
                    .setMaxCategories(40)
                    .fit(trainData);
            featureIndexer.write().overwrite().save("models\\vector");
            System.out.println("FEATURE indexer saved");


            /*VectorIndexerModel featureIndexerTest = new VectorIndexer()
                    .setInputCol(generated_feature_col_name)
                    .setOutputCol(indexedFeatures)
                    .setMaxCategories(40)
                    .fit(testData);*/

            RandomForestClassifier rf = new RandomForestClassifier()
                    .setLabelCol(indexedLabel)
                    .setFeaturesCol(indexedFeatures)
                    .setSeed(seed)
                    .setNumTrees(noOfTrees);
            featureIndexer.write().overwrite().save("models\\rf");
            System.out.println("rf saved");
            // Convert indexed labels back to original labels.
            IndexToString labelConverter = new IndexToString()
                    .setInputCol(prediction)
                    .setOutputCol(predictedLabel)
                    .setLabels(labelIndexer.labels());
            featureIndexer.write().overwrite().save("models\\IndexToString");
            System.out.println("IndexToString saved");

            Pipeline pipeline = new Pipeline()
                    .setStages(new PipelineStage[] {labelIndexer, featureIndexer, rf, labelConverter});

            pipeline.write().overwrite().save("models\\Pipeline");
            System.out.println("Pipeline saved");
            return pipeline;
        } catch (IOException e) {
            System.out.println("====================================");
            System.out.println("CANNOT SAVE Pipeline ");
            System.out.println("====================================");
            e.printStackTrace();
        }
        System.out.println("Pipeline saved");

        return null;
    }

}
