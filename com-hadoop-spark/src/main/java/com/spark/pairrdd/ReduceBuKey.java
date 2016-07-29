package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 把key相同元素的value进行合并相加
 */
public class ReduceBuKey
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("ReduceBuKey");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> rdd = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md");
        List<String> rddList = rdd.collect();
        for(String s : rddList)
        {
            System.out.println(s);
        }

       JavaRDD<String> rddx =   rdd.flatMap(new FlatMapFunction<String, String>()
       {
           public Iterator<String> call(String s) throws Exception
           {
               return Arrays.asList(s.split(" ")).iterator();
           }
       });

        List<String> rddxList = rddx.collect();
        for(String s : rddxList)
        {
            System.out.println(s);
        }

        JavaPairRDD<String,Integer> pairRDD =  rddx.mapToPair(new PairFunction<String, String, Integer>()
        {
            public Tuple2<String, Integer> call(String s) throws Exception
            {
                return new Tuple2(s, 1);
            }
        });



                                                                        //pairRDD中v的类型，两个计算类型
        JavaPairRDD<String,Integer> reduceByKeyRDD = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>()
        {
            public Integer call(Integer x, Integer y) throws Exception
            {
                return new Integer(x + y);
            }
        });




        Map<String,Integer> reduceByKeyMap = reduceByKeyRDD.collectAsMap();
        long pairCount = pairRDD.count();
        long reduceByKeyCount = reduceByKeyRDD.count();
        System.out.println(pairCount);
        System.out.println(reduceByKeyCount);
        for(Map.Entry<String,Integer> entry : reduceByKeyMap.entrySet())
        {
            System.out.println(entry.getKey()+","+entry.getValue());
        }


    }
}
