package com.dl4j.diff.demo;

import com.google.common.collect.Lists;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.nd4j.autodiff.listeners.impl.ScoreListener;
import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.autodiff.samediff.TrainingConfig;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.weightinit.impl.XavierInitScheme;

import java.io.IOException;

/**
 * 自动微分
 */
public class LinearRegressionDiff
{
    public static void main(String[] args) throws IOException
    {
        //构建上下文实例
        SameDiff sd = SameDiff.create();


        SDVariable input = sd.placeHolder("input", DataType.FLOAT, -1, 1);
        SDVariable leabel = sd.placeHolder("label", DataType.FLOAT, -1, 1);

        SDVariable weights = sd.var("weights", new XavierInitScheme('c', 1, 1), DataType.FLOAT, 1, 1);
        SDVariable bias = sd.var("bias", DataType.FLOAT, 1);

        SDVariable predicted = input.mmul(weights).add("predicted", bias);

        sd.loss.meanSquaredError("mseloss", leabel, predicted);

        double learningRate = 1e-2;
        TrainingConfig config = new TrainingConfig.Builder()
                .updater(new Sgd(learningRate))
                .dataSetFeatureMapping("input")
                .dataSetLabelMapping("label")
                .build();
        sd.setTrainingConfig(config);
        sd.setListeners(new ScoreListener(1));

        INDArray data = Nd4j.readNumpy("/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv", ",");
        INDArray feature = data.getColumns(0);
        INDArray label = data.getColumns(1);

        DataSet ds = new DataSet(feature, label);

        SplitTestAndTrain train_test = ds.splitTestAndTrain(0.7);
        DataSet dsTrain = train_test.getTrain();
        DataSet dsTest = train_test.getTest();
        DataSetIterator trainIter = new ListDataSetIterator<DataSet>(Lists.newArrayList(dsTrain), 10);
        DataSetIterator testIter = new ListDataSetIterator<DataSet>(Lists.newArrayList(dsTest), 10);


        sd.fit(trainIter, 200);


        String outputVariable = predicted.getVarName();

        RegressionEvaluation evaluation = new RegressionEvaluation();
        sd.evaluate(testIter, outputVariable, evaluation);
        System.out.println(evaluation.stats());
    }
}
