package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import java.util.Iterator;
import java.util.List;

/**
 * rdd.union 合并两个RDD里面的内容，形成一个新的RDD
 */
public class Union
{


//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("RDD union");
        conf.setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> inputRDD =  jsc.textFile("hdfs://lan:8020/titanic/README.md");
        JavaRDD<String> toRDD = inputRDD.filter(new Function<String, Boolean>()
        {
            public Boolean call(String s) throws Exception
            {
                return s.contains("to");
            }
        });

        JavaRDD<String> onRDD = inputRDD.filter(new Function<String, Boolean>()
        {
            public Boolean call(String s) throws Exception
            {
                return s.contains("on");
            }
        });

        JavaRDD<String> resultRDD = toRDD.union(onRDD);

        //在本地迭代
       Iterator<String> it =  resultRDD.toLocalIterator();

        while (it.hasNext())
        {
            System.out.println(it.next());
        }


    }
}
