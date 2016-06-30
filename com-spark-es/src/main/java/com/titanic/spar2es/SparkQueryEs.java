package com.titanic.spar2es;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

/**
 * spark链接es查询
 */
public class SparkQueryEs
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf sc = new SparkConf().setMaster("spark://titanic-Lenovo:7077").setAppName("SparkForEs");
        sc.set("es.nodes", "192.9.7.4");
        sc.set("es.port", "9200");
        sc.set("es.http.timeout", "5m");
        JavaSparkContext jsc = new JavaSparkContext(sc);
        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-es/target/com-spark-es-1.jar");
        SQLContext sqlContent = new SQLContext(jsc.sc());
        DataFrame df = sqlContent.read().format("org.elasticsearch.spark.sql").load("test_bin_es/test_bin_es_type");
        df.show();
        jsc.stop();
    }
}
