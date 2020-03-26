package com.spark.data.analysis;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Describe
{
    public static String path = "/home/titanic/soft/intellij_workspace/github-hadoop/com-spark-data-analysis/src/main/resources/catering_sale.csv";

    public static void main(String[] args)
    {
        SparkSession spark = SparkSession.builder().
                master("spark://titanic:7077").
                appName("Describe").
                getOrCreate();

        Dataset<Row> data = spark
                .read()
                .option("header", "true")
                .format("com.databricks.spark.csv")
                .load(path);

        data.describe("销量").show();

        spark.stop();
    }
}
