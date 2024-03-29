/*******************************************************************************
 * Copyright (c) 2015-2019 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package com.dl4j.spark.demo;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.spark.api.TrainingMaster;
import org.deeplearning4j.spark.impl.multilayer.SparkDl4jMultiLayer;
import org.deeplearning4j.spark.impl.paramavg.ParameterAveragingTrainingMaster;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * spark-submit \
 * --conf 'spark.executor.extraJavaOptions=-Dorg.bytedeco.javacpp.maxbytes=2G -Dorg.bytedeco.javacpp.maxphysicalbytes=3G -Daeron.term.buffer.length=33554432' \
 * --conf 'spark.driver.extraJavaOptions=-Dorg.bytedeco.javacpp.maxbytes=2G -Dorg.bytedeco.javacpp.maxphysicalbytes=3G -Daeron.term.buffer.length=33554432' \
 * --driver-java-options '-Dorg.bytedeco.javacpp.maxbytes=2G -Dorg.bytedeco.javacpp.maxphysicalbytes=3G -Daeron.term.buffer.length=33554432' \
 * --master yarn \
 * --class com.dl4j.spark.demo.MnistMLPExample \
 * /home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j-spark/target/com-dl4j-spark-1.0-SNAPSHOT.jar
 */


/**
 * Train a simple/small MLP on MNIST data using Spark, then evaluate it on the test set in a distributed manner
 * <p>
 * Note that the network being trained here is too small to make proper use of Spark - but it shows the configuration
 * and evaluation used for Spark training.
 * <p>
 * <p>
 * To run the example locally: Run the example as-is. The example is set up to use Spark local by default.
 * NOTE: Spark local should only be used for development/testing. For data parallel training on a single machine
 * (for example, multi-GPU systems) instead use ParallelWrapper (which is faster than using Spark for training on a single machine).
 * See for example MultiGpuLenetMnistExample in dl4j-cuda-specific-examples
 * <p>
 * To run the example using Spark submit (for example on a cluster): pass "-useSparkLocal false" as the application argument,
 * OR first modify the example by setting the field "useSparkLocal = false"
 *
 * @author Alex Black
 */
public class MnistMLPExample
{
    private static final Logger log = LoggerFactory.getLogger(MnistMLPExample.class);

    @Parameter(names = "-useSparkLocal", description = "Use spark local (helper for testing/running without spark submit)", arity = 1)
    private boolean useSparkLocal = true;

    @Parameter(names = "-batchSizePerWorker", description = "Number of examples to fit each worker with")
    private int batchSizePerWorker = 16;

    @Parameter(names = "-numEpochs", description = "Number of epochs for training")
    private int numEpochs = 2;

    public static void main(String[] args) throws Exception
    {
        new MnistMLPExample().entryPoint(args);
    }

    protected void entryPoint(String[] args) throws Exception
    {
        //Handle command line arguments
        JCommander jcmdr = new JCommander(this);
        try
        {
            jcmdr.parse(args);
        } catch (ParameterException e)
        {
            //User provides invalid input -> print the usage info
            jcmdr.usage();
            try
            {
                Thread.sleep(500);
            } catch (Exception e2)
            {
            }
            throw e;
        }

        SparkConf sparkConf = new SparkConf();

        if (useSparkLocal)
        {
            sparkConf.setMaster("local[*]");
        }
        sparkConf.setAppName("DL4J Spark MLP Example");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        //Load the data into memory then parallelize
        //This isn't a good approach in general - but is simple to use for this example
        DataSetIterator iterTrain = new MnistDataSetIterator(batchSizePerWorker, true, 12345);
        DataSetIterator iterTest = new MnistDataSetIterator(batchSizePerWorker, true, 12345);
        List<DataSet> trainDataList = new ArrayList<DataSet>();
        List<DataSet> testDataList = new ArrayList<DataSet>();
        while (iterTrain.hasNext())
        {
            trainDataList.add(iterTrain.next());
        }
        while (iterTest.hasNext())
        {
            testDataList.add(iterTest.next());
        }

        JavaRDD<DataSet> trainData = sc.parallelize(trainDataList);
        JavaRDD<DataSet> testData = sc.parallelize(testDataList);


        //----------------------------------
        //Create network configuration and conduct network training
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(12345)
                .activation(Activation.LEAKYRELU)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(0.1))// To configure: .updater(Nesterovs.builder().momentum(0.9).build())
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(28 * 28).nOut(500).build())
                .layer(new DenseLayer.Builder().nOut(100).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX).nOut(10).build())
                .build();

        //Configuration for Spark training: see http://deeplearning4j.org/spark for explanation of these configuration options
        TrainingMaster tm = new ParameterAveragingTrainingMaster.Builder(batchSizePerWorker)    //Each DataSet object: contains (by default) 32 examples
                .averagingFrequency(5)
                .workerPrefetchNumBatches(2)            //Async prefetching: 2 examples per worker
                .batchSizePerWorker(batchSizePerWorker)
                .build();

        //Create the Spark network
        SparkDl4jMultiLayer sparkNet = new SparkDl4jMultiLayer(sc, conf, tm);

        //Execute training:
        for (int i = 0; i < numEpochs; i++)
        {
            sparkNet.fit(trainData);
            log.info("Completed Epoch {}", i);
        }

        //Perform evaluation (distributed)
//        Evaluation evaluation = sparkNet.evaluate(testData);
        Evaluation evaluation = sparkNet.doEvaluation(testData, 64, new Evaluation(10))[0]; //Work-around for 0.9.1 bug: see https://deeplearning4j.org/releasenotes
        log.info("***** Evaluation *****");
        log.info(evaluation.stats());

        //Delete the temp training files, now that we are done with them
        tm.deleteTempFiles(sc);

        log.info("***** Example Complete *****");
    }
}
