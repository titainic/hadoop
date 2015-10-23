package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Map
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        List<Integer> list = new ArrayList<Integer>();

        for(int x = 0 ; x < 10 ; x++)
        {
            list.add(x);
        }

        SparkConf conf = new SparkConf().setAppName("RDD map").setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<Integer> mapRDD = jsc.parallelize(list);

        JavaRDD<Integer> resultRDD = mapRDD.map(new Function<Integer, Integer>()
        {
            public Integer call(Integer x) throws Exception
            {
                return x+1 ;
            }
        });
        List<Integer> resultList = resultRDD.collect();

        for(Integer i : resultList)
        {
            System.out.println(i);
        }
    }
}
