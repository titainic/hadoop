package com.titanic.math.view;

import com.titanic.math.utils.DataInitUtils;
import org.charts.dataviewer.DataViewer;
import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.api.trace.ScatterTrace;
import org.charts.dataviewer.api.trace.TraceConfiguration;
import org.charts.dataviewer.utils.TraceColour;
import org.charts.dataviewer.utils.TraceMode;

import java.util.ArrayList;
import java.util.List;

public class PointView
{
    public static String dataPath = "/home/titanic/soft/intellij_workspace/github-hadoop/com-java-view/src/main/resources/point1.csv";


    public static void main(String[] args) throws InterruptedException
    {
        List<Double> AxList = new ArrayList<Double>();
        List<Double> AyList = new ArrayList<Double>();

        List<Double> BxList = new ArrayList<Double>();
        List<Double> ByList = new ArrayList<Double>();

        DataInitUtils.loadVSCLogisticData(AxList, AyList, BxList, ByList, dataPath);

        createDataViewerPoint(AxList, AyList, BxList, ByList);
//        Thread.currentThread().join();
    }




    public static DataViewer createDataViewerPoint(List<Double> AxList, List<Double> AyList, List<Double> BxList, List<Double> ByList)
    {
        DataViewer dataviewer = new DataViewer("Point");

        DataViewerConfiguration config = new DataViewerConfiguration();
        config.setPlotTitle("Scatter Trace Example");
        config.setxAxisTitle("X Example 4");
        config.setyAxisTitle("Y Example 4");
        config.setxRange(-5,5);
        config.setyRange(-5,5);
//        config.showLegend(true);
        dataviewer.updateConfiguration(config);
        System.out.println(dataviewer.getUrl());
        PlotData plotData = new PlotData();

        plotData.addTrace(createScatterTraceWithConfig(AxList,AyList));
        plotData.addTrace(createScatterTrace(BxList,ByList));

        dataviewer.updatePlot(plotData);
        return dataviewer;
    }

    public static ScatterTrace<Double> createScatterTrace( List<Double> BxList, List<Double> ByList) {
        ScatterTrace<Double> scatterTrace = new ScatterTrace<Double>();
        scatterTrace.setxArray(BxList.toArray(new Double[BxList.size()]));
        scatterTrace.setyArray(ByList.toArray(new Double[ByList.size()]));
        scatterTrace.setTraceName("MyScatterTrace");
        scatterTrace.setTraceColour(TraceColour.PURPLE);
        scatterTrace.setTraceMode(TraceMode.MARKERS);
        return scatterTrace;
    }

    public static ScatterTrace<Double> createScatterTraceWithConfig(List<Double> AxList, List<Double> AyList) {

        ScatterTrace<Double> scatterTrace = new ScatterTrace<Double>();
        scatterTrace.setxArray( AxList.toArray(new Double[AxList.size()]));
        scatterTrace.setyArray(AyList.toArray(new Double[AyList.size()]));

        TraceConfiguration scatterConfig = new TraceConfiguration();
        scatterConfig.setTraceName("MyScatterTrace1");
        scatterConfig.setTraceColour(TraceColour.RED);
        scatterConfig.setTraceMode(TraceMode.MARKERS);

        scatterTrace.setConfiguration(scatterConfig);
        return scatterTrace;
    }


}
