package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *有时间在折腾
 */
public class GroupWith
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("GroupWith");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<String> rdd = jsc.textFile("file:///D:\\soft\\spark\\spark-1.5.1-bin-hadoop2.6\\spark-1.5.1-bin-hadoop2.6\\README.md");
        JavaPairRDD<String,Integer> pairRDD = rdd.flatMapToPair(new PairFlatMapFunction<String, String, Integer>()
        {
            public Iterator<Tuple2<String, Integer>> call(String s) throws Exception
            {
                List<Tuple2<String, Integer>> list  =new ArrayList<Tuple2<String, Integer>>();
                String[] sArray = s.split(" ");
                for(String ss : sArray)
                {
                    Tuple2 t = new Tuple2(ss,1);
                    list.add(t);
                }
                return list.iterator();
            }
        });

//        pairRDD.groupWith()
    }
}
