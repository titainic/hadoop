package com.spark.file;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by titanic on 16-6-29.
 */
public class SparkFilesTest
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf sc = new SparkConf().setMaster("spark://titanic-Lenovo:7077").setAppName("SparkFilesTest");
        JavaSparkContext jsc = new JavaSparkContext(sc);
        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> rdd = jsc.textFile("file:///home/titanic/soft/hadoop/spark-2.0.0-bin-hadoop2.6/README.md");
        JavaPairRDD<String,Integer> pairRDD = rdd.flatMapToPair(new PairFlatMapFunction<String, String, Integer>()
        {
            public Iterator<Tuple2<String, Integer>> call(String s) throws Exception
            {
                List<Tuple2<String, Integer>> list = new ArrayList<Tuple2<String, Integer>>();
                String[] arr = s.split(" ");
                for (String str : arr)
                {
                    Tuple2<String, Integer> tuple2 = new Tuple2<String, Integer>(str, 1);
                    list.add(tuple2);
                }
                return list.iterator();
            }
        });
        JavaPairRDD<String,Integer> rRDD =  pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>()
        {
            public Integer call(Integer v1, Integer v2) throws Exception
            {
                return v1 + v2;
            }
        });

        Map<String, Integer> map = rRDD.collectAsMap();
        System.out.println(map);

        jsc.stop();
    }
}
