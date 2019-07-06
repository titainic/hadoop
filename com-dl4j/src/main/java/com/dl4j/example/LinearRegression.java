package com.dl4j.example;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LinearRegression
{
    //学习速度（梯度下降速度）
    public double learningrate = 0.1d;

    //初始化
    public double k = 5;
    public double b = 8;

    public static void main(String[] args) throws IOException, InterruptedException, PythonExecutionException
    {
        LinearRegression model = new LinearRegression();

        //加载csv数据
        INDArray data = Nd4j.readNumpy("/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv", ",");
        INDArray xData = data.getColumn(0);
        INDArray yData = data.getColumn(1);


        //获取画图数据
        double[] xDataPlot = xData.dup().data().asDouble();
        double[] yDataPlot = yData.dup().data().asDouble();
        List xList = arrat2List(xDataPlot);
        List yList = arrat2List(yDataPlot);


        //迭代次数
        final int iterations = 1;
        for (int iter = 0; iter < iterations; ++iter)
        {
            model.fitSGD(xData, yData);
        }


        //算出k和b以后，计算出方程y=kx+b直线所需要点
        List<Double> yLine = new ArrayList<>();
        List<Double> xLine = new ArrayList<>();
        Double k = model.getK();
        Double b = model.getB();

        //x=(y-b)/k
        for (int i = 0; i < 1000; i++)
        {
            //y=kx+b
            Double yL = k * i + b;
            //x=(y-b)/k
            Double xL = (yL - b) / k;

            yLine.add(yL);
            xLine.add(xL);
        }


        //打印图形和拟合函数的直线
        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(xList, yList, "o");
        plt.plot().add(xLine, yLine);
        plt.title("lr");
        plt.xlim(0,10);
        plt.ylim(0,25);
        plt.legend();
        plt.show();



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
    public void fitSGD(INDArray xData, INDArray yData)
    {
        double[] xArray = xData.dup().data().asDouble();
        double[] yArray = yData.dup().data().asDouble();
        for (int i = 0; i < xArray.length; i++)
        {
            double y = yArray[i];
            double x = xArray[i];

            //k=k-2y(y-kx-b)(-x)a
            k = k + 2 * y * (y - k * x - b) * x * learningrate;

            //b=k-2y(y-kx-b)a
            b = b - 2 * y * (y - k * x - b) * learningrate;
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

    /**
     * 为了图形化准备数据
     *
     * @param data
     * @return
     */
    public static List<Double> arrat2List(double[] data)
    {
        List<Double> list = new ArrayList<Double>();
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                list.add(Double.valueOf(data[i]));
            }
        }
        return list;
    }

}
