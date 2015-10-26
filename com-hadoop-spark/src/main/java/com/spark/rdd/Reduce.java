package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;
import java.util.List;

/**
 * reduce将RDD中元素两两传递给输入函数，同时产生一个新的值，新产生的值与RDD中下一个元素再被传递给输入函数直到最后只有一个值为止。
 */
public class Reduce
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {

        SparkConf conf = new SparkConf();
        conf.setAppName("binend reduce");
        conf.setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Integer> aList = new ArrayList<Integer>();

        for(int i = 0 ; i <= 30; i++)
        {
            aList.add(i);
        }

        JavaRDD<Integer> rdd = jsc.parallelize(aList);
        Integer sum = rdd.reduce(new Function2<Integer, Integer, Integer>()
        {
            public Integer call(Integer s, Integer s2)
            {
                System.out.println("s -> " + s);
                System.out.println("s2 -> " + s2);
                return s+s2;
            }
        });

        System.out.println(sum);
    }
}
