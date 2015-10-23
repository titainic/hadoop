package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

/**
 * 获取整个RDD中的数据
 * 只有当本地单独一台机器的内存能完全存的下时，才可以用此方法
 */
public class Collect
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
        List<String> localList = cRDD.collect();

        System.out.println(localList.size()+">>>>>>>>>>>>>>>>>>>>>>>");

        for(String s : localList)
        {
            System.out.println(s);
        }

    }
}
