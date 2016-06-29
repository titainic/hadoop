package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 删除PairRDD中,ardd与brdd建相同的元素
 */
public class SubtractByKey
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("SubtractByKey");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Tuple2<Integer,Integer>> alist = new ArrayList<Tuple2<Integer, Integer>>();
        List<Tuple2<Integer,Integer>> blist = new ArrayList<Tuple2<Integer, Integer>>();

        for(int x = 0 ; x <= 20 ; x++)
        {
            Tuple2 t = new Tuple2(x,x);
            alist.add(t);
        }

        for(int x = 10 ; x <= 30 ; x++)
        {
            Tuple2 t = new Tuple2(x,x);
            alist.add(t);
        }


        JavaPairRDD<Integer,Integer> ardd = jsc.parallelizePairs(alist);
        JavaPairRDD<Integer,Integer> brdd = jsc.parallelizePairs(blist);

        JavaPairRDD<Integer,Integer> crdd = ardd.subtractByKey(brdd);

        Map<Integer,Integer> map = crdd.collectAsMap();
        System.out.println(map);
    }
}
