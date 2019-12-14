package com.titanic.math.matrix;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.nd4j.linalg.ops.transforms.Transforms.exp;

public class SoftmaxMath
{
    public static void main(String[] args)
    {
        INDArray input = Nd4j.create(new double[]{-1d, 0d, 1d, 2d});
        softmax(input);
    }

    public static void softmax(INDArray input)
    {
        INDArray ei = exp(input, true);
        INDArray ss = ei.div(exp(input, false).sumNumber().doubleValue());
        System.out.println(ss);
    }
}
