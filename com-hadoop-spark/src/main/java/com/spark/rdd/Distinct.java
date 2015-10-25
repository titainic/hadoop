package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 消除没有重复的值
 */
public class Distinct
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("binend flatMap");
        conf.setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<String> list = new ArrayList<String>();
        for(int i = 0 ; i <= 20 ;i++ )
        {
            if(i%2 ==0)
            {
              list.add(i+"");
              list.add(i+"");
            }

        }
        System.out.println("List里面的数据");
        for (String s : list)
        {
            System.out.println(s);
        }

        JavaRDD<String> dRDD = jsc.parallelize(list);

        //distinct要经过网络混洗，开销很大
        JavaRDD<String> rdd = dRDD.distinct();

        List<String> resList = rdd.collect();

        System.out.println("distinct之后的数据");
        for(String s : resList)
        {
            System.out.println(s);
        }
    }
}
