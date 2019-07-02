package com.dl4j.example;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;

public class LinearRegression
{
    public double learningrate = 0.1d;
    public double k;
    public double b;


    public static void main(String[] args) throws IOException, InterruptedException
    {
        LinearRegression model = new LinearRegression();

        INDArray data = Nd4j.readNumpy("/home/titanic/soft/workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv", ",");

        //初始化
        double k_label = 1.1;
        double b_label = 2.2;

        //h(x)=kx+b
        INDArray label = data.mul(k_label).add(b_label);

        //迭代次数
        final int iterations = 1;
        for (int iter = 0; iter < iterations; ++iter)
        {
            model.fitSGD(data, label);
//            System.out.println(loss);
        }
        System.out.println("k: " + model.getK());
        System.out.println("b: " + model.getB());

        System.out.println(label);
    }


    //全量梯度下降
//    public double fitBGD(INDArray trainData, INDArray labelData)
//    {
//        INDArray diff = labelData.sub(trainData.mul(k).add(b));
//        k = diff.dup().mu
//
//        System.out.println(diff);
//        return 0;
//    }

    //随机梯度下降
    public void fitSGD(INDArray trainData, INDArray labelData)
    {
//        double diff = 0.0;
        for (int index = 0; index < trainData.length(); ++index)
        {

            double label = labelData.getDouble(index);
            double data = trainData.getDouble(index);


            //h(x)=kx+b
            //k-2×a×(y-h(x))×x
            k = k + 2 * learningrate *(label - (k * data + b)) * data ;
            b = b + 2 * learningrate*(label - (k * data + b)) ;
        }

    }

    public double getK()
    {
        return k;
    }

    public void setK(double k)
    {
        this.k = k;
    }

    public double getB()
    {
        return b;
    }

    public void setB(double b)
    {
        this.b = b;
    }


}
