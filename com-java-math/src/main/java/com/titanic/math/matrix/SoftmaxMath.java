package com.titanic.math.matrix;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.nd4j.linalg.ops.transforms.Transforms.exp;

public class SoftmaxMath
{
    public static void main(String[] args)
    {
        INDArray input = Nd4j.create(new double[]{-1.0,0.0,1.0,2.0});
        softmax(input);
    }

    public static void softmax(INDArray input)
    {
        INDArray ei = exp(input, false);
        double ez = exp(input, false).sumNumber().doubleValue();
        INDArray result = ei.div(ez);
        System.out.println(result);
    }
}
