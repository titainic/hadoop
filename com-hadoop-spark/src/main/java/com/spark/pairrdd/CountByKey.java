package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PairRDD中,将key相同的元素的value合并
 */
public class CountByKey
{
    public  static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setMaster("spark://localhost:7077");
        conf.setAppName("CountByKey");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");


        JavaPairRDD<String, Integer> pairRDD = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md").flatMapToPair(new PairFlatMapFunction<String, String, Integer>()
        {
            public Iterable<Tuple2<String, Integer>> call(String s) throws Exception
            {
                List<Tuple2<String, Integer>> list = new ArrayList<Tuple2<String, Integer>>();
                String[] string = s.split(" ");
                for(String str : string)
                {
                    Tuple2 t = new Tuple2(str,1);
                    list.add(t);
                }
                return list;
            }
        });

       Map<String, Object> map = pairRDD.countByKey();
        System.out.println(map);




    }
}
