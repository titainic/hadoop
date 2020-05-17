package com.spark.data.analysis;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import static org.apache.spark.sql.functions.col;

public class DistributionAnalysis
{
    public static String path = "/home/titanic/soft/pycharm_workspace/python-project-ai/data/analysis/catering_fish_congee.csv";

    public static void main(String[] args)
    {
        SparkSession spark = SparkSession.builder().
                master("spark://titanic:7077").
                appName("DistributionAnalysis").
                getOrCreate();

        Dataset<Row> data = spark
                .read()
//                .option("header", "true")
                .format("com.databricks.spark.csv")
                .load(path);

        data.show();
        data.printSchema();
        data.select(col("_c1").cast(DataTypes.IntegerType)).describe("_c1").show();

        spark.stop();
    }
}
