package com.dl4j.example;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.List;


public class PlotViewDemo
{
    public static void main(String[] args)
    {

    }

    public static void data(String title, List<Double> x, List<Double> y, double theta0, double theta1){
        DefaultXYDataset xydataset = new DefaultXYDataset ();

        for(double i=4 ; i < 25.0 ; i+=0.01) {
            x.add(i);
            y.add(theta0 + theta1*i);
        }

        double[][] data=new double[2][x.size()];
        for(int i=0;i<y.size();i++)
        {
            data[0][i]=Math.abs(x.get(i));
            data[1][i]=Math.abs(y.get(i));
        }

        xydataset.addSeries("Method", data);

        final JFreeChart chart =ChartFactory.createScatterPlot("ScattlePlot","X","Y",xydataset,PlotOrientation.VERTICAL,true,true,false);
        chart.setBackgroundPaint(ChartColor.WHITE);
        XYPlot xyplot = (XYPlot) chart.getPlot();
        xyplot.setBackgroundPaint(new Color(255, 253, 246));
        XYDotRenderer xydotrenderer = new XYDotRenderer();
        xydotrenderer.setDotWidth(3);
        xydotrenderer.setDotHeight(3);
        xyplot.setRenderer(xydotrenderer);


        ChartFrame frame = new ChartFrame(title,chart);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }
}
