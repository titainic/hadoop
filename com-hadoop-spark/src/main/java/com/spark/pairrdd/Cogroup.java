package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PairRDD中,将相同键Key的数据分组到一起
 */
public class Cogroup
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("Cogroup");
        conf.setMaster("local");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        //jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Tuple2<Integer,Integer>> alist = new ArrayList<Tuple2<Integer, Integer>>();
        List<Tuple2<Integer,Integer>> blist = new ArrayList<Tuple2<Integer, Integer>>();

        for(int x = 0 ; x < 20; x++)
        {

            Tuple2 tx = new Tuple2(x,x);
            alist.add(tx);
        }

        for(int x = 18 ; x < 30; x++)
        {
            Tuple2 t = new Tuple2(x,x);
            blist.add(t);
        }
        JavaPairRDD<Integer,Integer> ardd = jsc.parallelizePairs(alist);
        JavaPairRDD<Integer,Integer> brdd = jsc.parallelizePairs(blist);

        JavaPairRDD<Integer, Tuple2<Iterable<Integer>, Iterable<Integer>>> crdd = ardd.cogroup(brdd);

        Map<Integer, Tuple2<Iterable<Integer>, Iterable<Integer>>> map =crdd.collectAsMap();
        System.out.println(map);

    }
}
