package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.hive.HiveContext;

/**
 *  spark链接hive,测试通过
 *  version:
 *          hadoop2.6.4
 *          spark1.6.1
 *          hive2.0
 */
public class SparkSqlForHive
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setMaster("spark://titanic-Lenovo:7077").setAppName("SparkSqlForHive");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        System.setProperty("hive.metastore.uris", "thrift://127.0.0.1:9083");

        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-sql/target/com-spark-sql-0.0.1-SNAPSHOT.jar");
        HiveContext sqlContext = new HiveContext(jsc.sc());
        DataFrame df = sqlContext.sql("select * from default.test_bin");
        df.show();
        jsc.stop();
    }

}
