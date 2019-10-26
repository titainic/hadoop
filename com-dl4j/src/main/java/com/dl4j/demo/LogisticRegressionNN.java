package com.dl4j.demo;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.nd4j.linalg.activations.Activation.SIGMOID;

/**
 * DL4J神经网络实现逻辑回归
 */
public class LogisticRegressionNN
{
    public static String path = "/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/logistic.csv";

    public static void main(String[] args) throws IOException
    {
        MultiLayerConfiguration conf = new NeuralNetConfiguration
                .Builder()
                .seed(123456)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.1))
                .list()
                .layer(0, new OutputLayer
                        .Builder(LossFunctions.LossFunction.MSE)
                        .activation(SIGMOID)
                        .nIn(2)
                        .nOut(1)
                        .build())
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);

        net.init();

        Table data = Table.read().csv(path);

        DataSetIterator train = getData(data);

        for (int i = 0 ;i< 200;i++)
        {
            net.fit(train);
        }

        Map<String, INDArray> param = net.paramTable();
        param.forEach((key, value) -> System.out.println("key:" + key +", value = " + value));

        double[] theta = new double[3];
        theta[0] = param.get("0_W").getDouble(0);
        theta[1] = param.get("0_W").getDouble(1);
        theta[2] = param.get("0_b").getDouble(0);


        Layout layout = Layout.builder()
                .title("LogisticRegression")
                .xAxis(Axis.builder().build())
                .yAxis(Axis.builder().build())
                .build();
        List<Double> AxList = new ArrayList<>();
        List<Double> AyList = new ArrayList<>();
        List<Double> BxList = new ArrayList<>();
        List<Double> ByList = new ArrayList<>();

        DataInitUtils.loadVSCLogisticData(AxList, AyList, BxList, ByList, path);

        double[] ax = Nd4j.create(AxList).data().asDouble();
        double[] ay = Nd4j.create(AyList).data().asDouble();
        double[] bx = Nd4j.create(BxList).data().asDouble();
        double[] by = Nd4j.create(ByList).data().asDouble();

        //直线x的坐标
        List<Double> xListLines = new ArrayList<>();
        for (int i = -6; i <= 10; i++)
        {
            xListLines.add(Double.valueOf(i));
        }


        List<Double> yList =viewY(xListLines, theta);

        double[] xLine = Nd4j.create(xListLines).data().asDouble();
        double[] yLine = Nd4j.create(yList).data().asDouble();

        ScatterTrace line = ScatterTrace.builder(xLine, yLine).mode(ScatterTrace.Mode.LINE).build();
        Trace aPolt = ScatterTrace.builder(ax,ay).marker(Marker.builder().color("rgb(17, 157, 255)").build()).build();
        Trace bPolt = ScatterTrace.builder(bx,by).marker(Marker.builder().color("rgb(231, 99, 250)").build()).build();
        Plot.show(new Figure(layout, aPolt,bPolt,line));

    }

    /**
     * 获取ｙ的坐标
     * @param xList
     * @param argsTheta
     * @return
     */
    public static List<Double> viewY(List<Double> xList, double[] argsTheta)
    {
        double theta1 = argsTheta[0];
        double theta2 = argsTheta[1];
        double theta3 = argsTheta[2];
        List<Double> yList = new ArrayList<>();

        double k = -theta1 / theta2;
        double b = -theta3 / theta2;

        for (int i = 0; i < xList.size(); i++)
        {
            double y = k * xList.get(i) + b;
            yList.add(y);
        }
        return yList;
    }

    /**
     * 构造数据
     * @param data
     * @return
     */
    public static DataSetIterator getData(Table data)
    {
        List<Double> x1List = (List<Double>) data.column(0).asList();
        List<Double> x2List = (List<Double>) data.column(1).asList();
        List<Double> yList = (List<Double>) data.column(2).asList();

        INDArray x1 = Nd4j.create(x1List).reshape(new int[]{x1List.size(),1});
        INDArray x2 = Nd4j.create(x2List).reshape(new int[]{x1List.size(),1});
        INDArray y = Nd4j.create(yList).reshape(new int[]{x1List.size(),1});

        INDArray x = Nd4j.hstack(x1, x2);

        DataSet ds = new DataSet(x, y);
        List<DataSet> listDS = ds.asList();
        DataSetIterator dsi = new ListDataSetIterator(listDS, 10);
        return dsi;
    }
}
