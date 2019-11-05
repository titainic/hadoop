package com.feature.view.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.Histogram;

import java.util.List;

import static org.apache.spark.sql.functions.col;

public class ViewBarTraceExample
{


    public static void main(String[] args)
    {


        SparkSession spark = SparkSession.builder().
                master("spark://titanic:7077").
                appName("直方图").
                getOrCreate();


        Dataset<Row> dataSet = spark.read().json("file:///home/titanic/soft/intellij_workspace/github-hadoop/com-feature/src/main/resources/yelp_academic_dataset_business.json");

        List<Row> list = dataSet.select(col("review_count")).collectAsList();

        double[] data = row2double(list);
        spark.stop();

        Plot.show(Histogram.create("点击数量直方图", data));
    }


    public static double[] row2double(List<Row> data)
    {
        double[] list = new double[data.size()];
        for (int i = 0; i < data.size(); i++)
        {
            list[i] = Double.parseDouble(data.get(i).mkString());
        }
        return list;
    }

}
