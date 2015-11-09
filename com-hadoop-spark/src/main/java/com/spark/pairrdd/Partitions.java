package com.spark.pairrdd;

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
 * 查看ＲＤＤ的分区
 */
public class Partitions
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("Partitions");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        //最后的参数１０，并行的的分区，应该是分配１０个任务
        JavaRDD<String> dataRDD = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md", 10);

        //查看ＲＤＤ分区数
        int dataRDDPartitions = dataRDD.partitions().size();
        System.out.println(dataRDDPartitions);

        JavaPairRDD<String,Integer> toPairRDD = dataRDD.flatMapToPair(new PairFlatMapFunction<String, String, Integer>()
        {
            public Iterable<Tuple2<String, Integer>> call(String s) throws Exception
            {
                String[] tempStr = s.split(" ");
                List<Tuple2<String, Integer>> list =new  ArrayList<Tuple2<String, Integer>>();
                for(String str : tempStr)
                {
                    Tuple2 t = new Tuple2(str,1);
                    list.add(t);
                }
                return list;
            }
        });
        Map<String,Integer> map = toPairRDD.collectAsMap();
        System.out.println(map);

        int x = toPairRDD.partitions().size();
        System.out.println(x);

        //此操作是降低RDD的分区数量，即降低并行的任务数量.不是很确定
        JavaPairRDD<String,Integer> coalesceRDD = toPairRDD.coalesce(5);
        int c = coalesceRDD.partitions().size();
        System.out.println(c);

    }


}
