package com.titanic.ml.lr;

import com.titanic.ml.utils.DataInitUtils;
import com.titanic.ml.utils.PlotViewUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nd4j.linalg.ops.transforms.Transforms.abs;
import static org.nd4j.linalg.ops.transforms.Transforms.exp;

/**
 * 逻辑回归DL4J实现
 */
public class LogisticRegressionDL4J
{
    public static String dataPath = LogisticRegressionDL4J.class.getClassLoader().getResource("logistic.csv").getFile();

    //初始化学习速度（梯度下降速度）
    public static double learningrate = 0.001d;

    //迭代次数
    public static int maxIterations = 1000;

    public static double epsilon = 0.001d;

    public static void main(String[] args) throws IOException
    {
        INDArray data = Nd4j.readNumpy(dataPath, ",");

        List<Double> AxList = new ArrayList<>();
        List<Double> AyList = new ArrayList<>();

        List<Double> BxList = new ArrayList<>();
        List<Double> ByList = new ArrayList<>();


        INDArray xi = data.getColumns(0,1);
        INDArray y = data.getColumn(0);

        INDArray xii = Nd4j.ones(1, xi.size(0));

        //构建X矩阵
        INDArray X = Nd4j.hstack(xi, xii.transpose());

        INDArray theta = training(learningrate,X,y,maxIterations,epsilon);

        System.out.println(theta);

        DataInitUtils.loadVSCLogisticData(AxList,AyList,BxList,ByList,dataPath);

        PlotViewUtils.xyViewPoint(AxList,AyList,BxList,ByList,-5,6,-5,17);

    }



    /**
     * sigmoid矩阵形式
     * @param Z
     * @return
     */
    private static INDArray sigmoid(INDArray Z)
    {
        //Z = (-Z)
        Z = Z.mul(-1.0);
        //Z = e^(Z);
        Z = exp(Z, false);
        //1 + Z
        Z = Z.add(1.0);

        //反向除法的缩写
        //1.0 / Z
        Z = Z.rdiv(1.0);
        return Z;
    }

    /**
     * 线性回归矩阵形式
     * @param X
     * @param theta
     * @return
     */
    private static INDArray calculateOutput(INDArray X, INDArray theta)
    {
        INDArray z = X.mmul(theta);
        return sigmoid(z);
    }


    /**
     * 梯度计算
     * @param theta
     * @param X
     * @param y
     * @return
     */
    private static INDArray gradientFunction(INDArray theta, INDArray X, INDArray y)
    {
        //训练的样本总数
        double m = X.size(0);

        INDArray h = calculateOutput(X, theta);

        // h(x)-y_i
        INDArray diff = h.dup().sub(y);

        return X.dup().transpose().mmul(diff).mul(1.0 /  m);
    }

    /**
     * 训练
     * @param learningrate 学习率
     * @param X 样本数据矩阵
     * @param y 样本结果向量
     * @param maxIterations 最大迭代
     * @param epsilon
     * @return
     */
    private static INDArray training(double learningrate, INDArray X, INDArray y, int maxIterations, double epsilon)
    {
        //随机构建
        Nd4j.getRandom().setSeed(1234);

        //随机初始化theta
        INDArray theta = Nd4j.rand((int) X.size(1), 1);

        INDArray newTheta = theta.dup();

        //最优theta
        INDArray optimalTheta = theta.dup();
        for (int i = 0; i < maxIterations; i++)
        {
            INDArray gradients = gradientFunction(theta, X, y);

            //梯度下降计算
            gradients = gradients.mul(learningrate);
            newTheta = theta.sub(gradients);

            System.out.println("迭代次数:"+i+" theta->"+theta);

            //梯度计算的终止条件
            if (hasConverged(theta, newTheta, epsilon))
            {
                break;
            }
            theta = newTheta;

        }
        optimalTheta = newTheta;

        return optimalTheta;
    }

    /**
     * 当计算
     * @param oldTheta
     * @param newTheta
     * @param epsilon
     * @return
     */
    private static boolean hasConverged(INDArray oldTheta, INDArray newTheta, double epsilon)
    {
        double diffSum = abs(oldTheta.sub(newTheta)).sumNumber().doubleValue();
        return diffSum / (double) oldTheta.size(0) < epsilon;
    }

}
