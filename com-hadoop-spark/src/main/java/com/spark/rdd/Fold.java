package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;
import java.util.List;

/**
 * 和reduce一样,这个要提供初始值
 */
public class Fold
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {

        SparkConf conf = new SparkConf();
        conf.setAppName("binend fold");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Integer> aList = new ArrayList<Integer>();

        for(int i = 0 ; i <= 30; i++)
        {
            aList.add(i);
        }

       JavaRDD<Integer> rdd = jsc.parallelize(aList);

       Integer drdd = rdd.fold(1, new Function2<Integer, Integer, Integer>()
       {
           public Integer call(Integer integer, Integer integer2) throws Exception
           {
               return integer + integer2;
           }
       });
        System.out.println(drdd);

    }
}
