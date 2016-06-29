package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PairRDD里面每个value的操作
 */
public class MapValues
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("MapValues");
        conf.setMaster("local");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        List<Tuple2<String,Integer>> list = new ArrayList<Tuple2<String, Integer>>();
        list.add(new Tuple2("a", 1));
        list.add(new Tuple2("b", 1));
        list.add(new Tuple2("c", 1));
        list.add(new Tuple2("d", 1));
        list.add(new Tuple2("e", 1));
        list.add(new Tuple2("f", 1));

        JavaPairRDD<String,Integer> rdd = jsc.parallelizePairs(list);

        JavaPairRDD<String,Integer> pairRDD = rdd.mapValues(new Function<Integer, Integer>()
        {
            public Integer call(Integer integer) throws Exception
            {
                return integer+1;
            }
        });

        Map<String,Integer> map =pairRDD.collectAsMap();
        System.out.println(map);

    }
}
