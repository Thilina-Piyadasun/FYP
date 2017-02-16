package org.abithana.prediction;


import org.abithana.utill.Config;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.NaiveBayes;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilina on 8/13/2016.
 */
public class NaiveBaysianCrimeClassifier extends ClassificationModel {


    public NaiveBaysianCrimeClassifier(){

    }
    public NaiveBaysianCrimeClassifier(String[] feature_columns, String label) {
        this.testFeature_columns = feature_columns;
        this.feature_columns=feature_columns;
        this.label = label;
    }

    Pipeline getPipeline(DataFrame trainData){

        /*
        NaiveBayes nb = new NaiveBayes().setSmoothing(0.00001);
        Tokenizer tokenizer = new Tokenizer().setInputCol(label).setOutputCol(indexedLabel);
        HashingTF hashingTF = new HashingTF().setInputCol(tokenizer.getOutputCol()).setOutputCol(predictedLabel);
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[] {tokenizer, hashingTF, nb});*/
        try {
            StringIndexerModel labelIndexer = new StringIndexer()
                    .setInputCol(label)
                    .setOutputCol(indexedLabel)
                    .fit(trainData);

          //  labelIndexer.write().overwrite().save("./models/label");

            // Automatically identify categorical features, and index them.
            // Set maxCategories so features with > 4 distinct values are treated as continuous.
            VectorIndexerModel featureIndexer = new VectorIndexer()
                    .setInputCol(generated_feature_col_name)
                    .setOutputCol(indexedFeatures)
                    .setMaxCategories(40)
                    .fit(trainData);

            featureIndexer.write().overwrite().save("models\\vector");

            NaiveBayes nb = new NaiveBayes()
                    .setLabelCol(indexedLabel)
                    .setFeaturesCol(indexedFeatures);

            featureIndexer.write().overwrite().save("models\\naivebays");

            // Convert indexed labels back to original labels.
            IndexToString labelConverter = new IndexToString()
                    .setInputCol(prediction)
                    .setOutputCol(predictedLabel)
                    .setLabels(labelIndexer.labels());

            featureIndexer.write().overwrite().save("models\\IndexToString");

            Pipeline pipeline = new Pipeline()
                    .setStages(new PipelineStage[]{labelIndexer, featureIndexer, nb, labelConverter});

            featureIndexer.write().overwrite().save("models\\Pipeline");
            return pipeline;
        } catch (IOException e) {
            System.out.println("====================================================================");
            System.out.println("                      CANNOT SAVE Pipeline ");
            System.out.println("====================================================================");
            e.printStackTrace();
        }
        return null;
    }



}
