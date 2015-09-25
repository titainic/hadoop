package com.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * 初始化java版本的SparkContext
 */
public class SparkContextInit
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        String logFile = "hdfs://titanic:8020/titanic/README.md";
        //第一个参数表示运行方式（local、yarn-client、yarn-standalone等）
        //第二个参数表示应用名字
//        JavaSparkContext sc = new JavaSparkContext("yarn-client","SparkTest");
        SparkConf sc = new SparkConf().setAppName("Simple Application").setMaster("spark://binend:7077");
        JavaSparkContext jsc = new JavaSparkContext(sc);
//        jsc.addJar(String.valueOf(SparkContext.jarOfClass(SparkContextInit.class).toList()));
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-hdfs/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> logDta = jsc.textFile(logFile).cache();

        long numAs = logDta.filter(new Function<String, Boolean>()
        {
            public Boolean call(String s) throws Exception
            {
                return s.contains("a");
            }
        }).count();

        long numBs = logDta.filter(new Function<String, Boolean>()
        {
            public Boolean call(String s) throws Exception
            {
                return s.contains("b");
            }
        }).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
    }
}
