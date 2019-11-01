package com.dl4j.diff.demo;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.io.IOException;

public class PolynomialRegression
{
    public static String path = "/home/titanic/soft/intellij_workspace/github-hadoop/com-dl4j/src/main/resources/sin.txt";

    public static void main(String[] args) throws IOException
    {
        poltData();
    }

    public static void poltData() throws IOException
    {
        INDArray data = Nd4j.readNumpy(path, ",");

        double[] x = data.getColumns(0).data().asDouble();
        double[] y = data.getColumns(1).data().asDouble();

        Layout layout = Layout.builder()
                .title("多项式")
                .xAxis(Axis.builder().build())
                .yAxis(Axis.builder().build())
                .build();

        Trace polt = ScatterTrace.builder(x, y).marker(Marker.builder().opacity(.9).build()).build();

        Plot.show(new Figure(layout, polt));
    }
}
