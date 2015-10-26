package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 还没有完全学会
 */
public class Foreach
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("binend arrregate");
        conf.setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Integer> list = new ArrayList<Integer>();

        for(int i = 1 ; i <= 10 ; i++)
        {
            list.add(i);
        }

        JavaRDD<Integer> rdd = jsc.parallelize(list);
        rdd.toDebugString();

        rdd.foreach(new VoidFunction<Integer>()
        {
            public void call(Integer integer) throws Exception
            {
                System.out.println(integer);
            }
        });



        List<Integer> resList = rdd.collect();

        for(Integer i : resList)
        {
            System.out.println(i);
        }
    }
}
