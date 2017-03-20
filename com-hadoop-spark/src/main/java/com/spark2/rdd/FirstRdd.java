package com.spark2.rdd;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 最后异步ｃｏｌｌｅｃｔ的时候有问题
 */
public class FirstRdd implements Serializable
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {

        SparkSession spark = SparkSession.builder().master("spark://titanic-Lenovo:7077").appName("first spark").getOrCreate();
        JavaRDD<String> lines = spark.read().textFile("file:///home/titanic/soft/hadoop/spark-2.1.0-bin-hadoop2.7/README.md").javaRDD();
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>()
        {
            public Iterator<String> call(String s) throws Exception
            {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });

       JavaPairRDD<String,Integer> ones = words.mapToPair(new PairFunction<String,  String,Integer>()
       {
           public Tuple2<String,Integer > call(String s) throws Exception
           {

               Tuple2<String,Integer> t = new Tuple2<String,Integer>(s, 1);
               return t;
           }
       });

       JavaPairRDD<String,Integer> count = ones.reduceByKey(new Function2<Integer, Integer, Integer>()
       {
           public Integer call(Integer integer, Integer integer2) throws Exception
           {
               return integer+integer2;
           }
       });

        System.out.println(count.toDebugString());

        List<Tuple2<String, Integer>>  list = count.collect();

//        for (Tuple2<?, ?> tuple : list)
//        {
//            System.out.println(tuple._1()+":"+tuple._2());
//        }
        spark.stop();

    }
}
