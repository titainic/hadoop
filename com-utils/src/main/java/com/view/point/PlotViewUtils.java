package com.view.point;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.List;

public class PlotViewUtils
{
    /**
     * 根据x,y，一元先行回顾ｘ得到的ｙ，画出点和最优化直线
     * @param xList
     * @param yList
     * @param yuuu
     */
    public static void xyViewAndLine(List<Double> xList,List<Double> yList,List<Double> yuuu,double xStart,double xEnd,double yStart,double yEnd,String loss)
    {
        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(xList, yList, "o");
        plt.plot().add(xList, yuuu);
        plt.title("Loss="+loss);
        plt.xlim(xStart, xEnd);
        plt.ylim(yStart, yEnd);
        plt.legend();
        try
        {
            plt.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (PythonExecutionException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 根据ｘ和ｙ的数据画出坐标系，并打印点
     * @param xList
     * @param yList
     * @param xStart
     * @param xEnd
     * @param yStart
     * @param yEnd
     */
    public static void xyViewPoint(List<Double> xList, List<Double> yList, int xStart, int xEnd, int yStart, int yEnd)
    {
        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(xList, yList, "o");
        plt.title("lr");
        plt.xlim(xStart, xEnd);
        plt.ylim(yStart, yEnd);
        plt.legend();
        try
        {
            plt.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (PythonExecutionException e)
        {
            e.printStackTrace();
        }
    }


    public static void printxLine(List<Double>... listDoub)
    {
        Plot plxt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));

        for(int i = 1 ;i < listDoub.length;i++)
        {
            plxt.plot().add(listDoub[0], listDoub[i]);
        }

        plxt.xlim(0, 10);
        plxt.ylim(-2, 2);
        plxt.legend();
        try
        {
            plxt.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (PythonExecutionException e)
        {
            e.printStackTrace();
        }
    }


}
