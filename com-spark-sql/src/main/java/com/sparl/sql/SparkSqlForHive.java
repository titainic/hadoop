package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.hive.HiveContext;

/**
 * Created by wb-yangbin.d on 2015/11/19.
 */
public class SparkSqlForHive
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("SparkSqlForHive");


    }
}
