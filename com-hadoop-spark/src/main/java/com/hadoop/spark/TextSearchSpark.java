package com.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.List;

/**
 * 文本文件中，文字的查找
 */
public class TextSearchSpark
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf sc = new SparkConf();
        sc.setAppName("Text search");
        sc.setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(sc);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-hdfs/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> textFile = jsc.textFile("hdfs://lan:8020/titanic/xiaoshuo");

        JavaRDD<String> context = textFile.filter(new Function<String, Boolean>()
        {
            //查找 hdfs://lan:8020/titanic/xiaoshuo 这个文本文件里面的清字
            public Boolean call(String s) throws Exception
            {
                return s.contains("清");
            }
        });

        //查询这个rdd里面 “清” 字有多少行
        long  count = context.count();
        System.out.println(count);

        //每一个为String，的List
        List<String> cList =  context.collect();
        for(String s : cList)
        {
            System.out.println("->"+s);
        }

        //保存到hdfs  --》  有问题 10行一个文件 ？？
        context.saveAsTextFile("hdfs://lan:8020/titanic/xiashuo_2");
    }
}
