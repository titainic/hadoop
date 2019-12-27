package com.dl4j.book;

import com.dl4j.utils.DataInitUtils;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Saturn的非线性数据建模
 * 书的126页
 *
 */
public class MLPClassifierSaturn
{
    public static int batchSize = 50;
    public static int seed = 123;
    public static double learningRate = 0.005;
    public static int nEpochs = 500;
    public static int numInput=2;
    public static int numOutput = 2;
    public static int numHiddenNodes = 20;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        String fileNameTrain = new ClassPathResource("classification/saturn_data_train.csv").getFile().getPath();
        String fileNameTest = new ClassPathResource("classification/saturn_data_eval.csv").getFile().getPath();

        //加载训练数据
        RecordReader rrTrain = new CSVRecordReader();
        rrTrain.initialize(new FileSplit(new File(fileNameTrain)));
        DataSetIterator trainIterator = new RecordReaderDataSetIterator(rrTrain, batchSize, 0, 2);

        //加载测试数据
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File(fileNameTest)));
        DataSetIterator testIterator = new RecordReaderDataSetIterator(rrTest, batchSize, 0, 2);

        //构建网络配置和参数
        MultiLayerConfiguration conf = new NeuralNetConfiguration
                .Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd(learningRate))
                .list()
                .layer(0, new DenseLayer
                        .Builder()
                        .nIn(numInput)
                        .nOut(numOutput)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new OutputLayer
                        .Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX)
                        .nIn(numInput)
                        .nOut(numOutput)
                        .build())
                .build();

        //加载神经网络配置
        MultiLayerNetwork mode = new MultiLayerNetwork(conf);

        //初始化神经网络
        mode.init();

        //没更新10个参数就打印分数
        mode.setListeners(new ScoreIterationListener(10));

        for (int i = 0; i < nEpochs; i++)
        {
            mode.fit(trainIterator);
        }

        System.out.println("评估模型");
        Evaluation eval = new  Evaluation(numOutput);

        while (testIterator.hasNext())
        {
            DataSet t = testIterator.next();
            INDArray features = t.getFeatures();
            INDArray lables = t.getLabels();

            INDArray predicted = mode.output(features,false);
            eval.eval(lables, predicted);
        }

        System.out.println(eval.stats());

        //数据可视化
        List<Double> aXList = new ArrayList<>();
        List<Double> aYList = new ArrayList<>();
        List<Double> bXList = new ArrayList<>();
        List<Double> bYList = new ArrayList<>();

        DataInitUtils.loadVSCLogisticDataDL4J(aXList,aYList,bXList,bYList,fileNameTrain);

        double[] ax = Nd4j.create(aXList).data().asDouble();
        double[] ay = Nd4j.create(aYList).data().asDouble();
        double[] bx = Nd4j.create(bXList).data().asDouble();
        double[] by = Nd4j.create(bYList).data().asDouble();

        Layout layout = Layout.builder()
                .title("MLPClassifierSaturn")
                .xAxis(Axis.builder().build())
                .yAxis(Axis.builder().build())
                .build();

        Trace aPolt = ScatterTrace.builder(ax,ay).marker(Marker.builder().color("rgb(17, 157, 255)").build()).build();
        Trace bPolt = ScatterTrace.builder(bx,by).marker(Marker.builder().color("rgb(255, 157, 17)").build()).build();
        Plot.show(new Figure(layout, aPolt,bPolt));
    }
}
