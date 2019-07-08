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

    public static void main(String[] args) throws IOException, PythonExecutionException
    {
        LinearRegression model = new LinearRegression();

        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        DataInputStream in = new DataInputStream(new FileInputStream(new File(LinearRegression.class.getClassLoader().getResource("lr2.csv").getFile())));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String csvRow;
        while ((csvRow = bufferedReader.readLine()) != null)
        {
            String[] strArr = csvRow.split(",");
            xList.add(Double.valueOf(strArr[0]));
            yList.add(Double.valueOf(strArr[1]));
        }

        //迭代次数
        final int iterations = 100;
        for (int iter = 0; iter < iterations; ++iter)
        {
            model.gradientDescient(xList, yList,model);

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





    public void gradientDescient(List<Double> xData, List<Double> yData,LinearRegression model)
    {
        double sum0 = 0.0;
        double sum1 = 0.0;

        for (int i = 0; i < xData.size(); i++)
        {
            double x = xData.get(i);
            double y = yData.get(i);

            sum0 += calc_error(x,y);
            sum1 += calc_error(x, y) * x;
        }

        this.b = b - learningrate * sum0 / xData.size();
        this.k = k - learningrate * sum1 / xData.size();

        double loss = 0;

        for(int c = 0 ;c < xData.size();c++)
        {
            double x = xData.get(c);
            double y = yData.get(c);
            loss+= Math.pow(calc_error(x,y),2);

        }

        String lossSub = loss + "";
        lossSub = lossSub.substring(0, 4);

//        BigDecimal bigLoss = new BigDecimal(lossSub);
//        BigDecimal minkb = new BigDecimal("2");
//
//
//        if((k+b)< 5)
//        {
//            if (bigLoss.compareTo(minkb) == -1)
//            {
//                model.setB(b);
//                model.setK(k);
//                return true;
//            }
//        }

        System.out.println("loss->"+loss);
//        return false;
    }

    //目标函数
    public double predict(double x)
    {
        return k * x + b;
    }

    //误差值
    public double calc_error(double x, double y)
    {
        return predict(x) - y;
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