package com.spark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wb-yangbin.d on 2015/11/10.
 */
public class PartitionBy
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("PartitionBy");

        JavaSparkContext jsc = new JavaSparkContext(conf);


//        JavaRDD<String> rdd = jsc.textFile("file:///D:\\soft\\spark\\spark-1.5.1-bin-hadoop2.6\\spark-1.5.1-bin-hadoop2.6\\README.md");
//        JavaPairRDD<String,Integer> pairRDD = rdd.flatMapToPair(new PairFlatMapFunction<String, String, Integer>()
//        {
//            @Override
//            public Iterable<Tuple2<String, Integer>> call(String s) throws Exception
//            {
//                List<Tuple2<String, Integer>> list  =new ArrayList<Tuple2<String, Integer>>();
//                String[] sArray = s.split(" ");
//                for(String ss : sArray)
//                {
//                    Tuple2 t = new Tuple2(ss,1);
//                    list.add(t);
//                }
//                return list;
//            }
//        });
//
//        Map<String, Integer> map = pairRDD.collectAsMap();
//        System.out.println(map);


    }
}
