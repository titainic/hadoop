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
    public double learningrate = 0.1d;
    public double k;
    public double b;

    public static List<Double> xList = new ArrayList<Double>();
    public static List<Double> yList = new ArrayList<Double>();




    public static void main(String[] args) throws IOException, InterruptedException, PythonExecutionException
    {
        LinearRegression model = new LinearRegression();

        //加载csv数据
        INDArray data = Nd4j.readNumpy("/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv", ",");
//--------------------------------------------------------------------
        //获取画图数据
        INDArray xData= data.getColumn(0);
        INDArray yData= data.getColumn(1);
        double[] xDataPlot = xData.dup().data().asDouble();
        double[] yDataPlot = yData.dup().data().asDouble();
        List xList = arrat2List(xDataPlot);
        List yList = arrat2List(yDataPlot);
//-------------------------------------------------------------

        //算法步骤

        //初始化
        double k_label = 1.1;
        double b_label = 2.2;

        //h(x)=kx+b
        INDArray label = data.mul(k_label).add(b_label);

        //迭代次数
        final int iterations = 1;
        for (int iter = 0; iter < iterations; ++iter)
        {
            model.fitSGD(data, label);
        }

        System.out.println("k: " + model.getK());
        System.out.println("b: " + model.getB());
        System.out.println(label);
//------------------------------------------------------------
        //图形设置
        Plot plt =  Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(xList, yList,"o");//.add(Arrays.asList(model.getK(),model.getB()));////
        plt.title("lr");
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
    public void fitSGD(INDArray trainData, INDArray labelData)
    {
        for (int index = 0; index < trainData.length(); ++index)
        {

            double label = labelData.getDouble(index);
            double data = trainData.getDouble(index);


            //h(x)=kx+b
            //k-2×a×(y-h(x))×x
            k = k + 2 * learningrate *(label - (k * data + b)) * data ;
            b = b + 2 * learningrate*(label - (k * data + b)) ;
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
