package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wb-yangbin.d on 2015/11/11.
 */
public class Pregel
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("Pregel");
        conf.setMaster("local");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        List<Tuple2<String,Integer>> aList = new ArrayList<Tuple2<String, Integer>>();
        aList.add(new Tuple2("a",1));
        aList.add(new Tuple2("b",1));
        aList.add(new Tuple2("c",1));
        aList.add(new Tuple2("d",1));
        aList.add(new Tuple2("e",1));
        aList.add(new Tuple2("f",1));

        List<Tuple2<String,Integer>> bList = new ArrayList<Tuple2<String, Integer>>();
        bList.add(new Tuple2("a1",2));
        bList.add(new Tuple2("b1",2));
        bList.add(new Tuple2("c1",2));
        bList.add(new Tuple2("d1",2));
        bList.add(new Tuple2("e1",2));
        bList.add(new Tuple2("f1",2));

        JavaPairRDD<String,Integer> vertices  = jsc.parallelizePairs(aList);
        JavaPairRDD<String,Integer> messages  = jsc.parallelizePairs(bList);

        JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> grouped  = vertices.cogroup(messages);

        JavaPairRDD<String, Integer> newData = grouped.mapValues(new Function<Tuple2<Iterable<Integer>, Iterable<Integer>>, Integer>()
        {
            @Override
            public Integer call(Tuple2<Iterable<Integer>, Iterable<Integer>> iterableIterableTuple2) throws Exception
            {
                return null;
            }
        });



    }
}
