package com.spark.sql2;

import org.apache.spark.sql.SparkSession;

/**
 * spark 2.0链接hive
 */
public class SparkSqlForHive
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkSession spark = SparkSession.builder()
                .master("spark://titanic-Lenovo:7077")
                .appName("spark sql for hive").getOrCreate();

        System.setProperty("hive.metastore.uris", "thrift://127.0.0.1:9083");
    }
}
