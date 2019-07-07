package com.dl4j.example;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
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

        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        DataInputStream in = new DataInputStream(new FileInputStream(new File("/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv")));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String csvRow;
        while ((csvRow = bufferedReader.readLine()) != null)
        {
            String[] strArr = csvRow.split(",");
            xList.add(Double.valueOf(strArr[0]));
            yList.add(Double.valueOf(strArr[1]));
        }

        //迭代次数
        final int iterations = 1;
        for (int iter = 0; iter < iterations; ++iter)
        {
            model.fitSGD(xList, yList);
        }


        //算出k和b以后，计算出方程y=kx+b直线所需要点d的集合
        List<Double> yLine = new ArrayList<>();
        String kstr = model.getK() + "";
        model.setK(Double.valueOf(kstr.substring(0, 15)));
        String bstr = model.getB() + "";
        model.setB(Double.valueOf(bstr.substring(0, 15)));

        Double k = model.getK();
        Double b = model.getB();


        for (int i = 0; i < xList.size(); i++)
        {
            double x = xList.get(i);
            //y=kx+b
            Double yL = k * x + b;
            double r2 = Math.pow((yL - yList.get(i)), 2);
            yLine.add(yL);
        }
        List<Double> yuuu = doublesData(yLine);

        //打印图形和拟合函数的直线
        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(xList, yList, "o");
        plt.plot().add(xList, yuuu);
        plt.title("lr");
        plt.xlim(0, 10);
        plt.ylim(0, 25);
        plt.legend();
        plt.show();

    }




    //梯度下降
    public void fitSGD(List<Double> xData, List<Double> yData)
    {
        List<LRLoss> lrList = new ArrayList<>();

        for (int i = 0; i < xData.size(); i++)
        {
            double y = yData.get(i);
            double x = xData.get(i);

            //k=k-2y(y-kx-b)(-x)a
            k = k - (y - k * x - b) * x * learningrate;

            //b=b-2y(y-kx-b)a
            b = b - (y - k * x - b) * learningrate;

            double yll = k*x+b;
            double r2 = Math.pow((yll - yData.get(i)), 2);

            LRLoss loss = new LRLoss(k,b,r2);

            lrList.add(loss);
            System.out.println("Loss->"+r2);
            System.out.println("k->"+k);
            System.out.println("b->"+b);
            System.out.println("\n");
        }

    }



    public void minKB(List<LRLoss> lrList)
    {
        double sum = 0, min = lrList.get(0).getLoss(), max = min;
        for (int i = 0; i < lrList.size(); i++)
        {
            sum += lrList.get(i).getLoss();
            if (min > lrList.get(i).getLoss()) {
                min = lrList.get(i).getLoss();

            }
//            if (max < lrList.get(i).getLoss()) {
//                max = lrList.get(i).getLoss();
//            }
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

    public static List<Double> doublesData(List<Double> list)
    {
        DecimalFormat df = new DecimalFormat("#.0000000000");
        List<Double> xLists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            String str = df.format(list.get(i));
            xLists.add(Double.valueOf(str));
        }
        return xLists;
    }

}
