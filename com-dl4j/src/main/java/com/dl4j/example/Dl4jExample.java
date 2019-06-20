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

//        add();
        System.out.println();
        mul();

    }

    /**
     * 加法
     */
    public static void add(){
        INDArray data = Nd4j.create(new double[]{1.0,2.0});

        INDArray addInd = data.add(10.0);
        System.out.println(addInd);
        System.out.println(data);
    }

    /**
     * 乘法
     */
    public static void mul()
    {
        INDArray data = Nd4j.create(new double[]{1.0, 2.0});
        INDArray mData1 = data.mul(20.0);
        System.out.println("mul result: " + mData1);
        System.out.println("after mul result: " + data);


        INDArray data2 = Nd4j.create(new double[]{1.0, 2.0});
        INDArray mData3 = data.muli(data2);
        System.out.println("muli result: " + mData3);
        System.out.println("after muli result: " + data);




    }
}
