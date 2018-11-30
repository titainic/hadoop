package com.titanic.spark2x.es6x;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.spark.sql.api.java.JavaEsSparkSQL;

import static org.apache.spark.sql.functions.col;

/**
 * 公职人员
 */
public class CivilServants
{
    public static void main(String[] args)
    {
        SparkSession spark = SparkSession
                .builder()
                .appName("CivilServants")
//                .config("num-executors", "10")
                .config("spark.executor.cores", "4")
                .config("spark.cores.max", "20")
                .config("spark.debug.maxToStringFields", "100")
                .config("spark.driver.host", "192.168.1.121")
                .config("spark.executor.memory", "6G")
                .config("spark.driver.memory", "4G")
                .config("spark.defalut.parallelism", "60")
                .config("spark.sql.shuffle.partitions ", "60")
                .config("es.nodes", "192.168.1.231")
                .config("es.port", "9200")
                .config("spark.sql.broadcastTimeout", "100000")
                .config("spark.es.mapping.date.rich","false")
                .master("spark://es01:7077")
                .getOrCreate();

        Dataset<Row> ytkDS = JavaEsSparkSQL.esDF(spark, "ykt201803_tmp").select(
                col("funds_code"),
                col("funds_name"),
                col("id_number"),
                col("money"),
                col("funds_year"),
                col("item_name"),
                col("xzqh_code"),
                col("grsqze"),
                col("open_by"),
                col("time"),
                col("open_time"));

        Dataset<Row> gzDS = JavaEsSparkSQL.esDF(spark, "base_gongzhi").select(
                col("xzqh"),
                col("csrq")   ,
                col("cjgzsj"),
                col("sfzh"));
        ytkDS.createOrReplaceTempView("ytkDS");
        gzDS.createOrReplaceTempView("gzDS");

        Dataset<Row> dataDS = ytkDS.sqlContext().sql("SELECT " +
                "t1.funds_code," +
                "t1.funds_name," +
                "t1.id_number," +
                "t1.money," +
                "t1.funds_year," +
                "t1.item_name," +
                "t1.xzqh_code," +
                "t1.grsqze," +
                "t1.open_by," +
                "t1.time," +
                "t1.open_time," +
                "t2.xzqh," +
                "t2.cjgzsj," +
                "t2.csrq" +
                " FROM ytkDS t1 ,gzDS t2 WHERE t1.id_number=t2.sfzh");

        dataDS.write()
                .format("org.elasticsearch.spark.sql")
                .option("es.nodes.wan.only","true")
//                .option("es.port","443")
//                .option("es.net.ssl","true")
                .option("es.nodes", "http://es01:9200")
                .mode("Overwrite")
                .save("result_gongzhi/doc");



        spark.stop();
    }
}
