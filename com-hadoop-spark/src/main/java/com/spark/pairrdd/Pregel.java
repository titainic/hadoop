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
 * Pregel算法，没搞懂
 */
public class Pregel
{
//    public static void main(String[] args)
//    {
//        init();
//    }

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
        bList.add(new Tuple2("a",2));
        bList.add(new Tuple2("b",2));
        bList.add(new Tuple2("c1",2));
        bList.add(new Tuple2("d1",2));
        bList.add(new Tuple2("e1",2));
        bList.add(new Tuple2("f1",2));

        JavaPairRDD<String,Integer> vertices  = jsc.parallelizePairs(aList);
        JavaPairRDD<String,Integer> messages  = jsc.parallelizePairs(bList);

        JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> grouped  = vertices.cogroup(messages);

        Map<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> groupedMap = grouped.collectAsMap();
        System.out.println(groupedMap);

        JavaPairRDD<String, Integer> newData = grouped.mapValues(new Function<Tuple2<Iterable<Integer>, Iterable<Integer>>, Integer>()
        {
            public Integer call(Tuple2<Iterable<Integer>, Iterable<Integer>> iterableIterableTuple2) throws Exception
            {
                int x = 0;
                if(iterableIterableTuple2._1.iterator().hasNext())
                {
                    x = iterableIterableTuple2._1.iterator().next().intValue()+3;
                }
                else
                {
                    return 3;
                }

                return x;
            }
        });

        Map<String, Integer> map = newData.collectAsMap();
        String Lineage =newData.toDebugString();
        System.out.println(Lineage);
        System.out.println(map);



    }
}
