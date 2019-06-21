package com.dl4j.spark.matrix;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

public class MatrixForDl4j
{
    public static void main(String[] args)
    {
        inversion();
    }


    /**
     * 矩阵求逆
     */
    public static void inversion()
    {

        double[][] matrixDouble = new double[][]{
                {3.0, 2.0},
                {-6.0, 6.0}};
        INDArray matrixA = Nd4j.create(matrixDouble);
        INDArray invA=InvertMatrix.invert(matrixA, false);
//        System.out.println(invA);


        double[][] doubB = new double[][]{
                {1.0, 0.0,1.0},
                {0.0, 2.0,1.0},
                {1.0,1.0,1.0}};
        INDArray matrixB = Nd4j.create(doubB);
        INDArray invB=InvertMatrix.invert(matrixB, false);
        INDArray iMatrix =invB.mul(matrixB);
        System.out.println(invB);
        System.out.println(iMatrix);

    }
}
