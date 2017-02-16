package org.abithana.prediction;


import org.abithana.utill.Config;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.DataFrame;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Thilina on 8/13/2016.
 */
public class MultilayerPerceptronCrimeClassifier extends ClassificationModel {

    int[] layers;
    int blockSize;
    long seed;
    int maxIterations;

    public MultilayerPerceptronCrimeClassifier(String[] feature_columns, String label, int[] layers, int blockSize, long seed) {
        this.testFeature_columns = feature_columns;
        this.feature_columns=feature_columns;
        this.label = label;
        this.layers=layers;
        this.blockSize=blockSize;
        this.maxIterations=maxIterations;
        this.seed=seed;
    }


    Pipeline getPipeline(DataFrame trainData){

        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol(label)
                .setOutputCol(indexedLabel)
                .fit(trainData);
        try {
            labelIndexer.write().overwrite().save("models\\label");
        } catch (IOException e) {
            System.out.println("====================================");
            System.out.println("CANNOT SAVE LABEL INDEXER");
            System.out.println("====================================");
            e.printStackTrace();
        }
        System.out.println("label indexer saved");
        // Automatically identify categorical features, and index them.
        // Set maxCategories so features with > 4 distinct values are treated as continuous.
        VectorIndexerModel featureIndexer = new VectorIndexer()
                .setInputCol(generated_feature_col_name)
                .setOutputCol(indexedFeatures)
                .setMaxCategories(40)
                .fit(trainData);

        try {
            featureIndexer.write().overwrite().save("models\\vector");
        } catch (IOException e) {
            System.out.println("====================================");
            System.out.println("CANNOT SAVE FEATURE INDEXER");
            System.out.println("====================================");
            e.printStackTrace();
        }
        System.out.println("FEATURE indexer saved");

        MultilayerPerceptronClassifier rf = new MultilayerPerceptronClassifier()
                .setPredictionCol(prediction)
                .setLayers(layers)
                .setBlockSize(blockSize)
                .setSeed(seed)
                .setMaxIter(maxIterations)
                .setLabelCol(indexedLabel)
                .setFeaturesCol(indexedFeatures)
                .setSeed(1100);
        try {
            featureIndexer.write().overwrite().save("models\\naivebays");
        } catch (IOException e) {
            System.out.println("====================================");
            System.out.println("CANNOT SAVE Multilayer perceptron classifier ");
            System.out.println("====================================");
            e.printStackTrace();
        }
        System.out.println("Multilayer perceptron  saved");
        // Convert indexed labels back to original labels.
        IndexToString labelConverter = new IndexToString()
                .setInputCol(prediction)
                .setOutputCol(predictedLabel)
                .setLabels(labelIndexer.labels());
        try {
            featureIndexer.write().overwrite().save("models\\IndexToString");
        } catch (IOException e) {
            System.out.println("====================================");
            System.out.println("CANNOT SAVE IndexToString ");
            System.out.println("====================================");
            e.printStackTrace();
        }
        System.out.println("IndexToString saved");

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] {labelIndexer, featureIndexer, rf, labelConverter});

        try {
            featureIndexer.write().overwrite().save("models\\Pipeline");
        } catch (IOException e) {
            System.out.println("====================================");
            System.out.println("CANNOT SAVE Pipeline ");
            System.out.println("====================================");
            e.printStackTrace();
        }
        System.out.println("Pipeline saved");

        return pipeline;

    }



}
