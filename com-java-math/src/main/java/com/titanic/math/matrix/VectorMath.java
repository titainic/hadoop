package com.titanic.math.matrix;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class VectorMath
{
    public static void main(String[] args)
    {
        vectorAdd();
    }

    public static void addi()
    {
        INDArray u = Nd4j.create(new double[]{1, 2, 3});
        INDArray v = Nd4j.create(new double[]{5, 6, 7});

        v.addi(u);

        System.out.println(v);

        u.muli(3);
        System.out.println(u);
    }

    /**
     * 向量內积
     */
    public static void dot()
    {
        INDArray u = Nd4j.create(new double[]{3, 5, 2});
        INDArray v = Nd4j.create(new double[]{1, 4, 7});

        u.muli(v);
        System.out.println(u.sumNumber().doubleValue());

    }

    /**
     * 向量外积
     */
    public static void cross()
    {
        INDArray u = Nd4j.create(new double[]{3, 5});
        INDArray v = Nd4j.create(new double[]{1, 4});


    }

    /**
     * 向量的线性组合
     */
    public static void vectorAdd()
    {
        INDArray u = Nd4j.create(new double[]{1, 2, 3});
        INDArray v = Nd4j.create(new double[]{4, 5, 6});
        INDArray w = Nd4j.create(new double[]{7, 8, 9});

        INDArray x = u.muli(3).add(v.muli(4)).add(w.muli(5)).dup();
        System.out.println(x);
    }
}
