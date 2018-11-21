//package com.titanic.spar2es;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
//
//import java.util.Map;
//
///**
// *
// * spark结合es，要把es-hadoop的jar在spark/conf/spark-env.sh中添加
// * export SPARK_CLASSPATH=$SPARK_CLASSPATH:/home/titanic/soft/hadoop/spark-1.6.1-bin-hadoop2.6/lib/elasticsearch-hadoop-2.3.2.jar
// * Created by titanic on 16-6-30.
// */
//public class SparkReadEs
//{
////    public static void main(String[] args)
////    {
////        init();
////    }
//
//    private static void init()
//    {
//
//        SparkConf sc = new SparkConf().setMaster("spark://titanic-Lenovo:7077").setAppName("SparkForEs");
//        sc.set("es.nodes", "192.9.7.4");
//        sc.set("es.port", "9200");
//        sc.set("es.http.timeout", "5m");
//        JavaSparkContext jsc = new JavaSparkContext(sc);
//        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-es/target/com-spark-es-1.jar");
//
//        JavaPairRDD<String, Map<String, Object>> esRDD = JavaEsSpark.esRDD(jsc, "test_bin_es/test_bin_es_type");
//        Map<String,Map<String,Object>> map = esRDD.collectAsMap();
//        System.out.println(map);
//
//
//        jsc.stop();
//    }
//}
