package com.dl4j.example;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import java.io.IOException;

public class MnistExample
{
    public static void main(String[] args) throws IOException
    {
        //矩阵的行数
        int numRow = 28;

        //矩阵的列数
        int numColumns= 28;

        //潜在结果（比如0到9的整数标签）的数量。
        int outoutNum =10;

        //每一步抓取的样例数量。
        int batchSize = 128;

        //这个随机数生成器用一个随机种子来确保训练时使用的初始权重维持一致。
        int rngSeed = 123;

        //一个epoch指将给定数据集全部处理一遍的周期。
        int numEpochs = 15;

        /**
         * 我们用名为DataSetIterator的类来抓取MNIST中的数据，
         * 创建一个用于模型训练的数据集mnistTrain，和另一个用于
         * 在训练后评估模型准确率的数据集mnistTest。顺便一提，此
         * 处的模型是指神经网络的各种参数。这些参数是用来处理输入数
         * 据的系数，神经网络在学习过程中不断调整参数，直至能准确预测
         * 出每一幅图像的标签――此时就得到了一个比较准确的模型。
         */
        DataSetIterator mnisTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
        DataSetIterator mnisTest = new MnistDataSetIterator(batchSize, false, rngSeed);

        MultiLayerConfiguration  conf = new NeuralNetConfiguration.Builder()
                /**
                 * 该参数将一组随机生成的权重确定为初始权重。如果一个示例运行很多次，
                 * 而每次开始时都生成一组新的随机权重，那么神经网络的表现（准确率和F1值）
                 * 有可能会出现很大的差异，因为不同的初始权重可能会将算法导向误差曲面上不
                 * 同的局部极小值。在其他条件不变的情况下，保持相同的随机权重可以使调整其
                 * 他超参数所产生的效果表现得更加清晰。
                 */
                .seed(rngSeed)

                /**
                 * 随机梯度下降
                 */
//                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)

                /**
                 * 动量（momentum）是另一项决定优化算法向最优值收敛的速度的因素。
                 * 动量影响权重调整的方向，所以在代码中，我们将其视为一种权重的更新器
                 */
                .updater(new Nesterovs(0.006, 0.9))

                /**
                 * 正则化（regularization）是用来防止过拟合的一种方法。过拟合是指模型对
                 * 训练数据的拟合非常好，然而一旦在实际应用中遇到从未出现过的数据，运行效果就变得很不理想。
                 * 我们用L2正则化来防止个别权重对总体结果产生过大的影响。
                 */
                .l2(1e-4)

                /**
                 * 函数可指定网络中层的数量；它会将您的配置复制n次，建立分层的网络结构
                 */
                .list(new DenseLayer.Builder()
                                    .nIn(numRow * numColumns)
                                    .nOut(1000)
                                    .activation(Activation.RELU)
                                    .weightInit(WeightInit.XAVIER)
                                    .build())
                .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                                    .nIn(1000)
                                    .nOut(outoutNum)
                                    .activation(Activation.SOFTMAX)
                                    .weightInit(WeightInit.XAVIER)
                                    .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);

        model.init();

        model.setListeners(new ScoreIterationListener(1));

        model.fit(mnisTrain,numEpochs);

        Evaluation eval = model.evaluate(mnisTest);

        System.out.println(eval);


    }
}
