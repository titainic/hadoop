package com.dl4j.nn.example;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SingleRegression
{

    //随机数种子，用于结果复现
    public static int seed = 123456;

    //对于每个minibatch的迭代次数
    public static int iterations = 10;

    //全部数据训练的次数
    public static int nEpochs = 200;

    //一共生成多少个样本点
    public static int nSamples = 1000;

    public static int batchSize = 100;

    public static double learningRate = 0.01;

    public static int minRange = 0;

    public static int macRange = 3;

    public static Random rng = new Random(seed);

    public static void main(String[] args)
    {
        int numInput = 1;
        int numOutputs = 1;

        //网络配置
        MultiLayerConfiguration conf = new NeuralNetConfiguration
                .Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(learningRate))
                .list()
                .layer(0,new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(numInput)
                        .nOut(numOutputs)
                        .build())
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);

        net.init();

        System.out.println(net.summary());

        net.setListeners(new ScoreIterationListener(1));


        DataSetIterator iterator = getTrainingData(batchSize, rng);

        for (int i = 0 ; i < nEpochs;i++)
        {
            iterator.reset();

            net.fit(iterator);

            Map<String, INDArray> params = net.paramTable();

            System.out.println("------------------");
            params.forEach((key, value) -> System.out.println("key:" + key +", value = " + value));
        }



        final INDArray input = Nd4j.create(new double[] { 10, 100 }, new int[] { 2, 1 });
        INDArray out = net.output(input, false);
        System.out.println(out);

//        net.setListeners(new StatsListener(statsStorage, listenerFrequency)
//                ,new ScoreIterationListener(1)
//        );
    }

    private static DataSetIterator getTrainingData(int batchSize, Random rand) {
        /**
         * 如何构造我们的训练数据
         * 现有的模型主要是有监督学习
         * 我们的训练集必须有  特征+标签
         * 特征-> x
         * 标签->y
         */
        double [] output = new double[nSamples];
        double [] input = new double[nSamples];
        //随机生成0到3之间的x
        //并且构造 y = 0.5x + 0.1
        //a -> 0.5  b ->0.1
        for (int i= 0; i< nSamples; i++) {
            input[i] = minRange + (macRange - minRange) * rand.nextDouble();

            output[i] = 0.5 * input[i] + 0.1;
        }

        /**
         * 我们nSamples条数据
         * 每条数据只有1个x
         */
        INDArray inputNDArray = Nd4j.create(input, new int[]{nSamples,1});

        INDArray outPut = Nd4j.create(output, new int[]{nSamples, 1});

        /**
         * 构造喂给神经网络的数据集
         * DataSet是将  特征+标签  包装成为一个类
         *
         */
        DataSet dataSet = new DataSet(inputNDArray, outPut);
        List<DataSet> listDs = dataSet.asList();

        return new ListDataSetIterator(listDs,batchSize);
    }
}
