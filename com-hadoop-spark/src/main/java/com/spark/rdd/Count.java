package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * RDD里面的行数
 */
public class Count
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setAppName("RDD count").setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> cRDD = jsc.textFile("hdfs://lan:8020/titanic/README.md");

        System.out.println(cRDD.count());

        for(String s : cRDD.take((int) (cRDD.count() -1)))
        {
            System.out.println(s);
        }
    }
}
