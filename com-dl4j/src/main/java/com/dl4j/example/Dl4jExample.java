package com.dl4j.example;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

public class Dl4jExample
{
    public static void main(String[] args)
    {
        //默认排序
        INDArray defaultOrderNd = Nd4j.create(new double[][]{{1.0, 2.0,3.0}, {4.0, 5.0,6.0}});
        double[] defaultOrderArray = defaultOrderNd.data().asDouble();
        System.out.println(Arrays.toString(defaultOrderArray));


        //c/c++排除（行的排序）
        INDArray cOrderNd = Nd4j.create(new double[][]{{1.0, 2.0,3.0}, {4.0, 5.0,6.0}},'c');
        double[] cOrderArray = cOrderNd.data().asDouble();
        System.out.println(Arrays.toString(cOrderArray));

        //c/c++排除（列的排序）
        INDArray fOrderNd = Nd4j.create(new double[][]{{1.0, 2.0,3.0}, {4.0, 5.0,6.0}},'f');
        double[] fOrderArray = fOrderNd.data().asDouble();
        System.out.println(Arrays.toString(fOrderArray));


    }
}
