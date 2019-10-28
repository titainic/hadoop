package com.dl4j.nn.demo;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 神经网络构建一元线性回归
 */
public class LinearRegressionNN
{
    public static String path = "/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv";

    //随机数种子，用于结果复现
    public static int seed = 123456;

    //全部数据训练的次数
    public static int nEpochs = 200;

    public static int batchSize = 10;

    public static double learningRate = 0.01;


    public static void main(String[] args) throws IOException
    {
        Layout layout = Layout.builder()
                .title("LinearRegression")
                .xAxis(Axis.builder().build())
                .yAxis(Axis.builder().build())
                .build();

        Table data = Table.read().csv(path);

        nnlr(data,layout);
    }

    /**
     * 神经网络实现一元线性回归
     * @param data
     * @param layout
     * @throws IOException
     */
    public static void nnlr(Table data,Layout layout) throws IOException
    {
        int numInput = 1;
        int numOutputs = 1;

        /**
         * 神经网络的构建
         */
        MultiLayerConfiguration conf = new NeuralNetConfiguration
                .Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(learningRate))
                .list()
                .layer(0,new OutputLayer
                        .Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(numInput)
                        .nOut(numOutputs).build())
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);

        net.init();

        DataSetIterator iterator = getTrainingData(batchSize,data);

        for(int i = 0 ; i < nEpochs ; i++)
        {
            net.fit(iterator);
        }
        Map<String, INDArray> param = net.paramTable();
        param.forEach((key, value) -> System.out.println("key:" + key +", value = " + value));

        double w = param.get("0_W").getDouble(0);
        double b = param.get("0_b").getDouble(0);
        System.out.println(w);
        System.out.println(b);

        List<Double> xList = (List<Double>) data.column(0).asList();

        double[] yArray = getY(w, b, xList);
        double[] xcc = Nd4j.create(xList).data().asDouble();

        plotData(data, layout, xcc,yArray);
    }

    /**
     * 根据w,b,x的值获取直线y的坐标
     * @param w
     * @param b
     * @param xList
     * @return
     */
    public static double[] getY(double w, double b, List<Double> xList)
    {
        double[] y = new double[xList.size()];
        for (int i = 0 ; i < xList.size() ;i++)
        {
            y[i] = xList.get(i) * w + b;
        }
        return y;
    }

    /**
     * 数据构建
     * @param batchSize
     * @param data
     * @return
     */
    private static DataSetIterator getTrainingData(int batchSize, Table data)
    {
        List<Double> xList = (List<Double>) data.column(0).asList();
        List<Double> yList = (List<Double>) data.column(1).asList();

        INDArray feayure = Nd4j.create(xList).reshape(new int[]{40,1});
        INDArray lable = Nd4j.create(yList).reshape(new int[]{40,1});

        DataSet dataSet = new DataSet(feayure,lable);
        List<DataSet> listDS = dataSet.asList();

        DataSetIterator dsi = new ListDataSetIterator(listDS, batchSize);

        return dsi;
    }

    /**
     * 可视化
     * @param data
     * @param layout
     * @param x
     * @param y
     * @throws IOException
     */
    public static void plotData(Table data, Layout layout,double[] x,double[] y) throws IOException
    {
        Trace polt = ScatterTrace.builder(data.column(0), data.column(1)).marker(Marker.builder().opacity(.9).build()).build();
        ScatterTrace line = ScatterTrace.builder(x, y).mode(ScatterTrace.Mode.LINE).build();
        Plot.show(new Figure(layout, polt,line));
    }
}
