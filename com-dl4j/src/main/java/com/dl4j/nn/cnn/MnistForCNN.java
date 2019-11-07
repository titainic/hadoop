package com.dl4j.nn.cnn;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 卷积神经网络实现mnist
 */
public class MnistForCNN
{
    private static final String BASE_PATH = "/home/titanic/soft/mnist";

    public static void main(String[] args) throws IOException
    {

        System.out.println(BASE_PATH + "/mnist_png");

        int height = 28;    // height of the picture in px
        int width = 28;     // width of the picture in px
        int channels = 1;   // single channel for grayscale images
        int outputNum = 10; // 10 digits classification
        int batchSize = 54; // number of samples that will be propagated through the network in each iteration
        int nEpochs = 1;    // number of training epochs

        Random randNumGen = new Random(1234);

        //加载训练数据并且向量化
        File trainData = new File(BASE_PATH + "/mnist_png/training");
        FileSplit trainSplit = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator(); // use parent directory name as the image label
        ImageRecordReader trainRR = new ImageRecordReader(height, width, channels, labelMaker);
        trainRR.initialize(trainSplit);
        DataSetIterator trainIter = new RecordReaderDataSetIterator(trainRR, batchSize, 1, outputNum);


        //数据归一化
        DataNormalization imageScaler = new ImagePreProcessingScaler();
        imageScaler.fit(trainIter);
        trainIter.setPreProcessor(imageScaler);

        //加载测试数据并且向量化
        File testData = new File(BASE_PATH + "/mnist_png/testing");
        FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ImageRecordReader testRR = new ImageRecordReader(height, width, channels, labelMaker);
        testRR.initialize(testSplit);
        DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, batchSize, 1, outputNum);
        testIter.setPreProcessor(imageScaler);


        //随着训练时间的增加而降低学习速度
        //迭代号，学习率
        Map<Integer, Double> learningRateSchedule = new HashMap<>();
        learningRateSchedule.put(0, 0.06);
        learningRateSchedule.put(200, 0.05);
        learningRateSchedule.put(600, 0.028);
        learningRateSchedule.put(800, 0.0060);
        learningRateSchedule.put(1000, 0.001);

        MultiLayerConfiguration conf = new NeuralNetConfiguration
                .Builder()
                .seed(1234)
                .l2(0.0005)
                .weightInit(WeightInit.XAVIER)

                // ? 更新器？梯度？
                .updater(new Nesterovs(new MapSchedule(ScheduleType.ITERATION, learningRateSchedule)))
                .list()
                .layer(0, new ConvolutionLayer                  //卷积层
                        .Builder(5, 5)                 //卷积大小5*5
                        .nIn(channels)                              //?　28*28 进行一维化784?进行输入 ?
                        .stride(1, 1)                                //卷积步长
                        .nOut(20)                                   //20个卷积核(过滤器)
                        .activation(Activation.IDENTITY).build())
                .layer(1, new SubsamplingLayer                 //池化层
                        .Builder(SubsamplingLayer.PoolingType.MAX)  //最大池化
                        .kernelSize(2, 2)                            //池化层大小为2*2
                        .stride(2, 2)                                //池化层步长
                        .build())
                .layer(2, new ConvolutionLayer                 //卷积层
                        .Builder(5, 5)                 //卷积大小5*5
                        .stride(1, 1)                                //卷积步长
                        .nOut(50)                                   //50个卷积核(过滤器)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(3, new SubsamplingLayer                  //池化层
                        .Builder(SubsamplingLayer.PoolingType.MAX)  //最大池化
                        .kernelSize(2, 2)                            //池化层大小为2*2
                        .stride(2, 2)                                //池化层步长
                        .build())
                .layer(4, new DenseLayer                        //全连接层
                        .Builder()
                        .activation(Activation.RELU)                //激活函数RELU
                        .nOut(500)                                  //500个输出，即神经节点
                        .build())
                .layer(5, new OutputLayer                      //输出层
                        .Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD) //　?
                        .nOut(outputNum)                            //分10分类
                        .activation(Activation.SOFTMAX)             //损失函数
                        .build())
                .setInputType(InputType.convolutionalFlat(height, width, channels))
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        net.setListeners(new ScoreIterationListener(10));

        for (int i = 0; i < nEpochs; i++)
        {
            net.fit(trainIter);
            Evaluation eval = net.evaluate(testIter);
            System.out.println(eval.stats());

            trainIter.reset();
            testIter.reset();
        }
        Map<String, INDArray> param = net.paramTable();
//        param.forEach((key, value) -> System.out.println("key:" + key + ", value = " + value));
        System.out.println(Arrays.toString(param.get("0_W").shape()));
        System.out.println(Arrays.toString(param.get("0_b").shape()));


    }
}
