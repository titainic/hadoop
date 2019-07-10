package com.titanic.ml.lr;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于DL4J，一元线性回归
 */
public class LinearRegressionDL4J
{
    public static String dataPath = LinearRegressionDL4J.class.getClassLoader().getResource("lr2.csv").getFile();

    //用于打印坐标系点
    public static List<Double> xList = new ArrayList<>();
    public static List<Double> yList = new ArrayList<>();

    //初始化斜率
    public double w = 5;

    //初始化截距
    public double b = 8;

    //初始化学习速度（梯度下降速度）
    public double learningrate = 8;

    public static int iteration = 10;



    //初始化加载图像数据
    public LinearRegressionDL4J()
    {
        DataInputStream in = null;
        try
        {
            in = new DataInputStream(new FileInputStream(new File(dataPath)));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String csvRow;
            while ((csvRow = bufferedReader.readLine()) != null)
            {
                String[] strArr = csvRow.split(",");
                xList.add(Double.valueOf(strArr[0]));
                yList.add(Double.valueOf(strArr[1]));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) throws IOException
    {
        LinearRegressionDL4J model = new LinearRegressionDL4J();

        //加载csv数据,用于计算
        INDArray data = Nd4j.readNumpy(dataPath, ",");
        INDArray xData = data.getColumn(0);
        INDArray yData = data.getColumn(1);

//        PlotViewUtils.xyViewPoint(xList,yList,0,10,0,25);

        for(int i = 0 ;i < iteration ; i++)
        {
            fitBGD(xData, yData, model);
        }

    }

    public static void fitBGD(INDArray x, INDArray y,LinearRegressionDL4J model)
    {
        double wt = model.getW();
        double bt = model.getB();
        double learningrate = model.getLearningrate();



        wt = wt - (x.muli(wt).add(bt).sub(y)).muli(x).mul(learningrate).sumNumber().doubleValue() / x.length();
        bt = bt - (x.muli(wt).add(bt).sub(y)).muli(learningrate).sumNumber().doubleValue() / x.length();

        double error = x.muli(wt).add(bt).sub(y).sumNumber().doubleValue();
        double loss = Math.pow(error, 2);

        System.out.println("w->"+wt);
        System.out.println("b->"+bt);
        System.out.println("loss->"+loss);



        model.setW(wt);
        model.setB(bt);
    }


    public double getLearningrate()
    {
        return learningrate;
    }

    public void setLearningrate(double learningrate)
    {
        this.learningrate = learningrate;
    }

    public double getW()
    {
        return w;
    }

    public void setW(double w)
    {
        this.w = w;
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
