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
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.io.IOException;

/**
 * 自动微分实现线性回归
 */
public class LinearRegressionDiff
{
    public static void main(String[] args) throws IOException
    {
        //特征数量
        int numFeautre = 1;

        //标签数量
        int numlabel = 1;

        //构建上下文实例
        SameDiff sd = SameDiff.create();


        SDVariable input = sd.placeHolder("input", DataType.FLOAT, -1, numFeautre); //-1是不定数据量，最后的1是特征数量ｘ
        SDVariable leabel = sd.placeHolder("label", DataType.FLOAT, -1, numlabel);
        //第一个１是特征数量x,第二个１是输出label１
        SDVariable weights = sd.var("weights", new XavierInitScheme('c', numFeautre, numlabel), DataType.FLOAT, numFeautre, numlabel);
        SDVariable bias = sd.var("bias", DataType.FLOAT, 1);

        SDVariable predicted = input.mmul(weights).add("predicted", bias);

        sd.loss.meanSquaredError("mseloss", leabel, predicted);

        double learningRate = 0.0001;
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
        DataSetIterator trainIter = new ListDataSetIterator<DataSet>(Lists.newArrayList(dsTrain), 100);
        DataSetIterator testIter = new ListDataSetIterator<DataSet>(Lists.newArrayList(dsTest), 100);

        sd.fit(trainIter, 10000);

        String outputVariable = predicted.getVarName();

        RegressionEvaluation evaluation = new RegressionEvaluation();
        sd.evaluate(testIter, outputVariable, evaluation);
        System.out.println(evaluation.stats());

        INDArray trainedWeights = sd.getVariable("weights").getArr();
        INDArray trainedBias = sd.getVariable("bias").getArr();
        System.out.println(String.format("Weights: %s, Bias: %s", trainedWeights, trainedBias));

        Layout layout = Layout.builder()
                .title("自动微分构建一元线性回归")
                .xAxis(Axis.builder().build())
                .yAxis(Axis.builder().build())
                .build();
        Trace polt = ScatterTrace.builder(feature.data().asDouble(), label.data().asDouble()).marker(Marker.builder().opacity(.5).build()).build();

        double[] x = feature.data().asDouble();
        double[] y = getY(trainedWeights.getDouble(0), trainedBias.getDouble(0), x);

        ScatterTrace line = ScatterTrace.builder(x,y).mode(ScatterTrace.Mode.LINE).build();
        Plot.show(new Figure(layout, polt,line));
    }

    public static double[] getY(double w, double b, double[] xList)
    {
        double[] y = new double[xList.length];
        for (int i = 0 ; i < xList.length ;i++)
        {
            y[i] = xList[i] * w + b;
        }
        return y;
    }
}
