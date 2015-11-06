package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Map;

/**
 *PairRDD创建，并且打印其内容
 */
public class NewPairRDD
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("new PairRDD");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");
        JavaRDD<String> lines = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md");
        JavaPairRDD<String,String> pairRDD = lines.mapToPair(new PairFunction<String, String, String>()
        {
            public Tuple2<String, String> call(String s) throws Exception
            {
                return new Tuple2(s.split(" ")[0], s);
            }
        });

        Map<String,String> map = pairRDD.collectAsMap();

        int count = 0;
        for(Map.Entry<String,String> entry : map.entrySet())
        {
            count++;
            System.out.println("第"+count+"行 k -> : " + entry.getKey()+" , \t v -> : "+entry.getValue());
        }

    }
}
