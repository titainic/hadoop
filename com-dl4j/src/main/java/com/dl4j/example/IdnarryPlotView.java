package com.dl4j.example;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdnarryPlotView
{
    public static void main(String[] args) throws IOException, PythonExecutionException
    {

        INDArray data = Nd4j.readNumpy("/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/lr2.csv", ",");
        //获取画图数据
        INDArray xData= data.getColumn(0)  ;
        INDArray yData= data.getColumn(1);
//        System.out.println(xData);
//        System.out.println(yData);

        double[] xDataPlot = xData.dup().data().asDouble();

        double[] yDataPlot = yData.dup().data().asDouble();
//
        List xList = arrat2List(xDataPlot);
        List yList = arrat2List(yDataPlot);




        Plot plt =  Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(xList,yList,"o");//.add(Arrays.asList(model.getK(), model.getB()));
        plt.text(50, 50, "text");
        plt.title("scatter");
        plt.show();
    }

    public static List<Double> arrat2List(double[] data)
    {


        List<Double> list = new ArrayList<Double>();
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                list.add(Arrays.asList(Double.valueOf(data[i])).get(0));
            }
        }
        return list;
    }
}
