package com.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by titanic on 15-11-6.
 */
public class WordCountTest1
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("test");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> lines = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md");

        List<String> linesList = lines.collect();

        System.out.println("linesList -V");
        for(String s : linesList)
        {
            System.out.println(s);
        }

        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>()
        {
            public Iterable<String> call(String s) throws Exception
            {
                return Arrays.asList(s.split(" "));
            }
        });

        List<String> wordsList = words.collect();

        System.out.println("wordsList -v");
        for(String s : wordsList)
        {
            System.out.println(s);
        }
                                                                        ///输入类型的rdd，输出k的类型，输出v的类型
        JavaPairRDD<String,Integer> ones =  words.mapToPair(new PairFunction<String, String, Integer>()
        {
        public Tuple2<String, Integer> call(String s) throws Exception
        {
            return new Tuple2(s,1);
        }
        });

//        Tuple2<String,Integer> tuple2 = ones.first();
//        System.out.println(tuple2);
//        System.out.println(tuple2._1);
//        System.out.println(tuple2._2);

//        System.out.println(tuple2._2);

        Map<String,Integer> onesMap = ones.collectAsMap();

        System.out.println("onesMap -v");
        for(Map.Entry<String,Integer> entry : onesMap.entrySet())
        {
            System.out.println("k -> : " + entry.getKey()+" , \t v -> : "+entry.getValue());
        }





    }
}
