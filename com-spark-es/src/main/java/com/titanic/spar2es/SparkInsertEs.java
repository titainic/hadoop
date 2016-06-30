package com.titanic.spar2es;

import com.google.common.collect.ImmutableList;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by titanic on 16-6-30.
 */
public class SparkInsertEs
{
    public static void main(String[] args)
    {
       init();
    }

    private static void init()
    {
        SparkConf sc = new SparkConf().setMaster("spark://titanic-Lenovo:7077").setAppName("SparkForEs");
        sc.set("es.nodes", "192.9.7.4");
        sc.set("es.port", "9200");
        sc.set("es.http.timeout", "5m");

        JavaSparkContext jsc = new JavaSparkContext(sc);
        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-es/target/com-spark-es-1.jar");

        List<TestBinBean> list = new ArrayList<TestBinBean>();
        list.add(new TestBinBean(5, "5",5 ,"5"));
        list.add(new TestBinBean(6, "6",6 ,"6"));
        list.add(new TestBinBean(7, "7",7 ,"7"));
        list.add(new TestBinBean(8, "8",8 ,"8"));


        JavaRDD<TestBinBean> rdd = jsc.parallelize(list);

        JavaEsSpark.saveToEs(rdd,"test_bin_es/test_bin_es_type");

        jsc.stop();
    }
}
