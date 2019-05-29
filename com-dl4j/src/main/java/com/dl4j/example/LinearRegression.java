package com.dl4j.example;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Random;

public class LinearRegression
{
    public static void main(String[] args)
    {
        lr();
    }

    public static void lr()
    {

        int exampleCount = 100;

        //学习速率
        double learningRate = 0.01;

        Random random = new Random();
        double[] data = new double[exampleCount * 3];
        double[] param = new double[exampleCount * 3];


        for (int i = 0; i < exampleCount * 3; i++)
        {
            data[i] = random.nextDouble();
        }

        for (int i = 0; i < exampleCount * 3; i++)
        {
            param[i] = 3;
            param[++i] = 4;
            param[++i] = 5;
        }

        INDArray features = Nd4j.create(data, new int[]{exampleCount, 3});
        INDArray params = Nd4j.create(param, new int[]{exampleCount, 3});

        INDArray label = features.mul(params).sum(1).add(10);

        double[] paramter = new double[]{1.0, 1.0, 1.0, 1.0};

        long startTime = System.currentTimeMillis();


        INDArray aDAta = features.getColumn(0);
        System.out.println(aDAta);

        for (int i = 0; i < 3000; i++)
        {
            BGD(features, label, learningRate, paramter);
        }

        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
    }

    public static void BGD(INDArray features, INDArray label, double learningRate, double[] parameter)
    {
        INDArray temp = features.getColumn(0).mul(parameter[0])
                .add(features.getColumn(1).mul(parameter[1]))
                .add(features.getColumn(2).mul(parameter[2])
                        .add(parameter[3]).sub(label));

        parameter[0] = parameter[0] - 2 * learningRate * temp.mul(features.getColumn(0)).sum(0).getDouble(0) / features.size(0);
        parameter[1] = parameter[1] - 2 * learningRate * temp.mul(features.getColumn(1)).sum(0).getDouble(0) / features.size(0);
        parameter[2] = parameter[2] - 2 * learningRate * temp.mul(features.getColumn(2)).sum(0).getDouble(0) / features.size(0);
        parameter[3] = parameter[3] - 2 * learningRate * temp.sum(0).getDouble(0) / features.size(0);

        INDArray functionResult = features.getColumn(0).mul(parameter[0]).add(features.getColumn(1).mul(parameter[1])).add(features.getColumn(2).mul(parameter[2])).add(parameter[3]).sub(label);// 用最新的参数计算总损失用

        double totalLoss = functionResult.mul(functionResult).sum(0).getDouble(0);

        System.out.println("totalLoss:" + totalLoss);
        System.out.println(parameter[0] + " " + parameter[1] + " " + parameter[2] + " " + parameter[3]);
    }
}
