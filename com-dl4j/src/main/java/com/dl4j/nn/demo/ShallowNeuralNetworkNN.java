package com.dl4j.nn.demo;

import com.dl4j.utils.DataInitUtils;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.nd4j.linalg.activations.Activation.SIGMOID;

/**
 * DL4J神经网络实现浅层神经网络
 */
public class ShallowNeuralNetworkNN
{

    public static String path = "/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/point1.csv";

    public static void main(String[] args) throws IOException
    {
        MultiLayerConfiguration conf = new NeuralNetConfiguration
                .Builder()
                .seed(123456)
                .weightInit(WeightInit.XAVIER)
                .optimizationAlgo(OptimizationAlgorithm.LINE_GRADIENT_DESCENT)
                .updater(new Sgd(0.1))
                .list()
                .layer(0,new DenseLayer.Builder()
                        .nIn(2)
                        .nOut(4)
                        .activation(SIGMOID)
                        .build())
                .layer(1,new OutputLayer
                        .Builder(LossFunctions.LossFunction.MSE)
                        .nIn(4)
                        .nOut(1)
                        .activation(SIGMOID).build())
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);

        net.init();
        net.setListeners(new ScoreIterationListener());

        DataSetIterator train = getData();

        //初始化用户界面后端
        UIServer uiServer = UIServer.getInstance();

        //设置网络信息（随时间变化的梯度、分值等）的存储位置。这里将其存储于内存。
        StatsStorage statsStorage = new InMemoryStatsStorage();         //或者： new FileStatsStorage(File)，用于后续的保存和载入

        //将StatsStorage实例连接至用户界面，让StatsStorage的内容能够被可视化
        uiServer.attach(statsStorage);

        //然后添加StatsListener来在网络定型时收集这些信息
        net.setListeners(new StatsListener(statsStorage));

        for (int i = 0 ;i< 10000;i++)
        {
            net.fit(train);
        }




        viewPointModel(net);

    }

    public static DataSetIterator getData() throws IOException
    {
        INDArray data = Nd4j.readNumpy(path,",");

        INDArray x = data.getColumns(0, 1);
        INDArray y = data.getColumns(2);

        DataSet ds = new DataSet(x, y);
        List<DataSet> listDS = ds.asList();
        DataSetIterator dsi = new ListDataSetIterator(listDS, 100);
        return dsi;
    }

    /**
     * 神经网络的平面打印
     *
     * @throws IOException
     */
    public static void viewPointModel(MultiLayerNetwork net) throws IOException
    {
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();

        DataInitUtils.loadCSVinitList(xList, yList, path);

        double xMax = Collections.max(xList);
        double xMix = Collections.min(xList);
        double yMax = Collections.max(yList);
        double yMin = Collections.min(yList);

        List<Double> xINDArrayList = new ArrayList<>();
        List<Double> yINDArrayList = new ArrayList<>();

        //根据x最小值和最大值，和y的最小值和最大值，取平面所有的点
        for (double i = xMix; i < xMax; i = i + 0.01)
        {
            for (double j = yMin; j < yMax; j = j + 0.01)
            {
                xINDArrayList.add(i);
                yINDArrayList.add(j);
            }
        }

        INDArray x1 = Nd4j.create(xINDArrayList);
        INDArray x2 = Nd4j.create(yINDArrayList);

        //把平面所有的点，组成x，y坐标，构建INDArray
        INDArray future = Nd4j.vstack(x1, x2).transpose();

        //使用训练好的网络，预测所有平面数据
        INDArray leable = net.output(future);

        INDArray yhat = sigmiodClassification(leable);

        double[] leableArray = yhat.data().asDouble();

        double[] xDouble = x1.data().asDouble();
        double[] yDouble = x2.data().asDouble();


        List<Double> AxList = new ArrayList<>();
        List<Double> AyList = new ArrayList<>();
        List<Double> BxList = new ArrayList<>();
        List<Double> ByList = new ArrayList<>();

//        //根据0，1分类，分类平面视图x,y
        for (int i = 0; i < leableArray.length; i++)
        {
            if (leableArray[i] == 0)
            {
                AxList.add(xDouble[i]);
                AyList.add(yDouble[i]);
            } else
            {
                BxList.add(xDouble[i]);
                ByList.add(yDouble[i]);
            }
        }

        List<Double> AxtrainList = new ArrayList<>();
        List<Double> AytrainList = new ArrayList<>();

        List<Double> BxtrainList = new ArrayList<>();
        List<Double> BytrainList = new ArrayList<>();

        DataInitUtils.loadVSCLogisticData(AxtrainList, AytrainList, BxtrainList, BytrainList, path);

        Layout layout = Layout.builder()
                .title("浅层神经网络")
                .xAxis(Axis.builder().build())
                .yAxis(Axis.builder().build())
                .build();

        double[] AxtrainIndarray = Nd4j.create(AxtrainList).data().asDouble();
        double[] AytrainIndarray = Nd4j.create(AytrainList).data().asDouble();
        double[] BxtrainIndarray = Nd4j.create(BxtrainList).data().asDouble();
        double[] BytrainIndarray = Nd4j.create(BytrainList).data().asDouble();

        double[] AxTestIndarray = Nd4j.create(AxList).data().asDouble();
        double[] AyTestIndarray = Nd4j.create(AyList).data().asDouble();
        double[] BxTestIndarray = Nd4j.create(BxList).data().asDouble();
        double[] ByTestIndarray = Nd4j.create(ByList).data().asDouble() ;

        Trace aTrainPolt = ScatterTrace.builder(AxtrainIndarray,AytrainIndarray).marker(Marker.builder().color("rgb(231, 99, 250)").build()).build();
        Trace bTrainPolt = ScatterTrace.builder(BxtrainIndarray,BytrainIndarray).marker(Marker.builder().color("rgb(34, 157, 255)").build()).build();
        Trace aTestPolt = ScatterTrace.builder(AxTestIndarray,AyTestIndarray).marker(Marker.builder().color("rgb(34, 157, 255)").build()).build();
        Trace bTestPolt = ScatterTrace.builder(BxTestIndarray,ByTestIndarray).marker(Marker.builder().color("rgb(231, 99, 250)").build()).build();

        Plot.show(new Figure(layout, aTrainPolt,bTrainPolt,aTestPolt,bTestPolt));
    }

    /**
     * sigmoid 0,1分类
     *
     * @param Z
     * @return
     */
    public static INDArray sigmiodClassification(INDArray Z)
    {
        double[] zArray = Z.data().asDouble();
        double[] dataArray = new double[zArray.length];
        for (int i = 0; i < zArray.length; i++)
        {
            if (zArray[i] > 0.5)
            {
                dataArray[i] = 1;
            } else
            {
                dataArray[i] = 0;
            }
        }
        INDArray yHat = Nd4j.create(dataArray);
        return yHat;
    }


}
