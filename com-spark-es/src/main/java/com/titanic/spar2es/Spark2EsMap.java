//package com.titanic.spar2es;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * spark结合es，要把es-hadoop的jar在spark/conf/spark-env.sh中添加
// * export SPARK_CLASSPATH=$SPARK_CLASSPATH:/home/titanic/soft/hadoop/spark-1.6.1-bin-hadoop2.6/lib/elasticsearch-hadoop-2.3.2.jar
// *
// * 以Ｍap方式插入数据到es
// */
//public class Spark2EsMap
//{
//    public static void main(String[] args)
//    {
//        init();
//    }
//
//    private static void init()
//    {
//
//        SparkConf sc = new SparkConf().setMaster("spark://titanic-Lenovo:7077").setAppName("SparkForEs");
//        sc.set("es.nodes", "192.9.7.4");
//        sc.set("es.port", "9200");
//        sc.set("es.http.timeout", "5m");
//
//        JavaSparkContext jsc = new JavaSparkContext(sc);
//        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-es/target/com-spark-es-1.jar");
//
//        List<Map<String, Object>> list = Lists.newArrayList();
//
//        Map<String, Object> m1 = Maps.newHashMap();
//        m1.put("id",9);
//        m1.put("name","9");
//        m1.put("tel","9");
//        m1.put("age",9);
//
//        list.add(m1);
//        JavaRDD<Map<String, Object>> rdd = jsc.parallelize(list);
//        JavaEsSpark.saveToEs(rdd,"test_bin_es/test_bin_es_type");
//        jsc.stop();
//
//    }
//}
