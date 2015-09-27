package com.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 使用java编写Spark版本的WordCount
 */
public class WordCountSpark
{
    private static final Pattern SPACE = Pattern.compile(" ");
    private static String SPARK_IP = "spark://binend:7077";
    private static String readmeFile = "hdfs://lan:8020/titanic/README.md";

//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setAppName("WordCount").setMaster(SPARK_IP);
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-hdfs/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> lines = sc.textFile(readmeFile);

        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>()
        {
            public Iterable<String> call(String s)
            {
                return Arrays.asList(SPACE.split(s));
            }
        });

        JavaPairRDD<String,Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>()
        {
            public Tuple2<String, Integer> call(String s) throws Exception
            {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        JavaPairRDD<String,Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>()
        {
            public Integer call(Integer integer, Integer integer2) throws Exception
            {
                return integer + integer2;
            }
        });

        List<Tuple2<String,Integer>> output = counts.collect();

        for(Tuple2<?,?> tuple:output)
        {
            System.out.println(tuple._1 + ": " + tuple._2);
        }
        sc.stop();
    }
}
