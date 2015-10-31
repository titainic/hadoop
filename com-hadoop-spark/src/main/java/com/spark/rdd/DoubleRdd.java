package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.DoubleFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码中有疑问
 */
public class DoubleRdd
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("binend doubleRDD");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/intelijWorkspace/github-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Integer> list = new ArrayList<Integer>();

        for(int x = 0 ; x <= 10 ; x++)
        {
            list.add(x);
        }

        JavaRDD<Integer> rdd = jsc.parallelize(list);
        JavaDoubleRDD doubleRDD = rdd.mapToDouble(new DoubleFunction<Integer>()
        {
            public double call(Integer x) throws Exception
            {
                return x * x;
            }
        });

        List<Double> collect = doubleRDD.collect();

        for(Double d : collect)
        {
            System.out.println(d);
        }

        System.out.println(doubleRDD.mean());
        System.out.println(doubleRDD.variance());
    }
}
