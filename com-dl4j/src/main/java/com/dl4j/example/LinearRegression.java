package com.dl4j.example;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;

public class LinearRegression
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        INDArray data = Nd4j.readNumpy("/home/titanic/soft/workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv", ",");
        System.out.println(data);
    }



}
