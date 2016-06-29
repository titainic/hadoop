package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 返回rdd中元素出现的次数
 */
public class CountByValue
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("binend countByValue");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/intelijWorkspace/github-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Integer> list = new ArrayList<Integer>();

        for(int x = 0 ; x <= 10 ; x++)
        {
            list.add(x);
        }

        JavaRDD<Integer> rdd = jsc.parallelize(list);
        Map<Integer,Long> map = rdd.countByValue();
        System.out.println(map);

    }
}
