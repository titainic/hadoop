package com.dl4j.spark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.spark.api.RDDTrainingApproach;
import org.deeplearning4j.spark.api.TrainingMaster;
import org.deeplearning4j.spark.impl.multilayer.SparkDl4jMultiLayer;
import org.deeplearning4j.spark.parameterserver.training.SharedTrainingMaster;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.parameterserver.distributed.conf.VoidConfiguration;

import java.io.IOException;
import java.util.List;

public class LinearRegressionNNSpark
{
    public static String path = "/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j-spark/src/main/resources/lr2.csv";

    public static void main(String[] args) throws IOException
    {
//        CudaEnvironment.getInstance().getConfiguration().allowMultiGPU(true);

        INDArray data = Nd4j.readNumpy(path, ",");
        DataSet dataSet = new DataSet(data.getColumn(0).reshape(new int[]{41, 1}), data.getColumn(1).reshape(new int[]{41, 1}));
        List<DataSet> trainData = dataSet.asList();


        SparkConf sparkConf = new SparkConf()
                .setAppName("LinearRegressionNNSpark");
//                .setMaster("spark://titanic:7077");
//        .setMaster("local[*]");
        sparkConf.set("spark.hadoop.fs.defaultFS", "hdfs://titanic:8020");
        sparkConf.set("spark.kryo.registrator", "org.nd4j.Nd4jSerializer");
//        sparkConf.set("spark.sql.broadcastTimeout", "1200");
//        sparkConf.set("spark.driver.memory ", "4g");
//        sparkConf.set("spark.executor.memory", "5g");
        sparkConf.set("spark.executor.extraJavaOptions", "-Dorg.bytedeco.javacpp.maxbytes=2921225472");
//        sparkConf.set()
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

//        sc.addJar("/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j-spark/target/com-dl4j-spark-1.0-SNAPSHOT.jar");

        JavaRDD<DataSet> trainRDD = sc.parallelize(trainData);

        MultiLayerConfiguration model = new NeuralNetConfiguration
                .Builder()
                .seed(12345)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.1))
                .list()
                .layer(0, new OutputLayer
                        .Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(1)
                        .nOut(1).build())
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(model);
        net.init();
        VoidConfiguration conf = VoidConfiguration.builder()
                .unicastPort(40123)             //Port that workers will use to communicate. Use any free port
                .networkMask("10.168.2.0")     //Network mask for communication. Examples 10.0.0.0/24, or 192.168.0.0/16 etc
                .controllerAddress("10.168.2.177")  //IP of the master/driver
                .build();

        TrainingMaster trainingMaster = new SharedTrainingMaster.Builder(conf, 4)
                .batchSizePerWorker(100) //Batch size for training
                .updatesThreshold(1e-3)                 //Update threshold for quantization/compression. See technical explanation page
                .workersPerNode(1)      // equal to number of GPUs. For CPUs: use 1; use > 1 for large core count CPUs
                .rddTrainingApproach(RDDTrainingApproach.Direct)
//                .meshBuildMode(MeshBuildMode.MESH)      // or MeshBuildMode.PLAIN for < 32 nodes
                .build();


        SparkDl4jMultiLayer sparkNet = new SparkDl4jMultiLayer(sc, net, trainingMaster);

        for (int i = 0; i < 200; i++)
        {
            sparkNet.fit(trainRDD);
        }

        sc.stop();
    }
}
