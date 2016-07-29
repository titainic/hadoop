package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *PairRDD,GroupBy操作，略懂吧，还不熟练
 */
public class GroupBy
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("GroupBy");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> data = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md");

        JavaPairRDD<String,Integer> pairRDD = data.flatMapToPair(new PairFlatMapFunction<String, String, Integer>()
        {
            public Iterator<Tuple2<String, Integer>> call(String s) throws Exception
            {
                String[] tempStr = s.split(" ");
                List<Tuple2<String, Integer>> list = new ArrayList<Tuple2<String, Integer>>();
                for (String str : tempStr)
                {
                    Tuple2 t = new Tuple2(str, 1);
                    list.add(t);
                }
                return list.iterator();
            }
        });
        JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> rdd = pairRDD.groupBy(new Function<Tuple2<String, Integer>, String>()
        {
            public String call(Tuple2<String, Integer> tuple2) throws Exception
            {

                return tuple2._1;
            }
        });

        Map<String, Iterable<Tuple2<String, Integer>>> map = rdd.collectAsMap();

        System.out.println(map);
    }
}
