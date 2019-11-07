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
 * 自动微分各种函数示例
 */
public class SameDiffExample
{


    public static void main(String[] args) throws IOException
    {

//        funationFx2();

//        multipleLinearRegression();

        funationFx();

    }

    /**
     * f(x) = e^x
     */
    public static void ePartialForX()
    {
        SameDiff sd = SameDiff.create();

        SDVariable x = sd.var("x");
        SDVariable f = sd.math.exp(x);

        //构建数据集
        INDArray data = Nd4j.arange(5, 20, 1);
        x.setArray(data);

        INDArray y = sd.batchOutput().output(f.getVarName()).input(x, data).execSingle();

        //公式结果
        System.out.println(y);

        sd.execBackwards(null);

        //导数
        System.out.println(x.getGradient().getArr());
    }

    /**
     * 多元线性回归示例，自动求梯度 r^2为负数有待验证
     * @throws IOException
     */
    public static void multipleLinearRegression() throws IOException
    {
        //特征数量
        int numFeature = 8;

        //标签数量
        int numlabel = 1;

        SameDiff sd = SameDiff.create();

        SDVariable inputX = sd.placeHolder("inputX", DataType.DOUBLE, -1, numFeature);
        SDVariable outputY = sd.placeHolder("outputY", DataType.DOUBLE, -1, numlabel);

        SDVariable weights = sd.var("weights", new XavierInitScheme('c', numFeature, numlabel), DataType.DOUBLE, numFeature, numlabel);
        SDVariable bias = sd.var("bias", DataType.DOUBLE, 1);

        SDVariable predicted = inputX.mmul(weights).add("predicted", bias);

        sd.loss.meanSquaredError("mseloss", outputY, predicted);

        double learningRate = 0.0001;
        TrainingConfig config = new TrainingConfig.Builder()
                .updater(new Sgd(learningRate))
                .dataSetFeatureMapping("inputX")
                .dataSetLabelMapping("outputY")
                .l2(0.001)
                .build();
        sd.setTrainingConfig(config);
        sd.setListeners(new ScoreListener(1));

        INDArray data = Nd4j.readNumpy("/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/asdew222.csv",",");
        INDArray feature = data.getColumns(0, 1, 2, 3, 4, 5, 6, 7);
        INDArray label = data.getColumns(8);
        DataSet ds = new DataSet(feature, label);

        SplitTestAndTrain train_test = ds.splitTestAndTrain(0.7);
        DataSet dsTrain = train_test.getTrain();
        DataSet dsTest = train_test.getTest();
        DataSetIterator trainIter = new ListDataSetIterator<DataSet>(Lists.newArrayList(dsTrain), 100);
        DataSetIterator testIter = new ListDataSetIterator<DataSet>(Lists.newArrayList(dsTest), 100);

        sd.fit(trainIter, 500);

        String outputVariable = predicted.getVarName();

        RegressionEvaluation evaluation = new RegressionEvaluation();
        sd.evaluate(testIter, outputVariable, evaluation);
        System.out.println(evaluation.stats());


        INDArray trainedWeights = sd.getVariable("weights").getArr();
        INDArray trainedBias = sd.getVariable("bias").getArr();
        System.out.println(String.format("Weights: %s, Bias: %s", trainedWeights, trainedBias));

    }


    public static void funationFx()
    {
        SameDiff sd = SameDiff.create();

        SDVariable inputX = sd.var("inputX");

        //构建函数
        SDVariable f = sd.math.sin(inputX.mul(2)).mul(sd.math.pow(sd.math.pow(inputX, 2).div(1), 3).add(1 / 2));

        INDArray data = Nd4j.create(new double[]{1,2,3});
        inputX.setArray(data);

        //向前传播
        INDArray y = sd.batchOutput().output(f.getVarName()).input(inputX, data).execSingle();

        System.out.println("正向传播");
        System.out.println(y);

        sd.execBackwards(null);

        //导数
        System.out.println(inputX.getGradient().getArr());
    }

    public static void funationFx2()
    {
        //构建SameDiff实例
        SameDiff sd=SameDiff.create();
        //创建变量x、y
        SDVariable x= sd.var("x");

        //定义函数
        SDVariable f = sd.math.pow(x, 2).mul(2);

        INDArray data = Nd4j.arange(1, 100, 1);
        //给变量var类型x绑定具体值
        x.setArray(data);

        //前向计算函数的值
        System.out.println(sd.batchOutput().output(f.getVarName()).input(f, data).execSingle());

        //后向计算求梯度
        sd.execBackwards(null);
        //打印导数
        System.out.println(x.getGradient().getArr());
    }


}
