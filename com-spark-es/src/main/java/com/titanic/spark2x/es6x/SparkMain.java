package com.titanic.spark2x.es6x;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.spark.sql.api.java.JavaEsSparkSQL;

import java.util.HashMap;
import java.util.Map;

/**
 * 要把elasticsearch-hadoop.6.4.3.jar拷贝到$SPARK_HOM/jars/下面
 */
public class SparkMain
{
    public static String url = "jdbc:oracle:thin:@//192.168.1.106:1521/orclpdb";
    public static String driver = "oracle.jdbc.driver.OracleDriver";

    public static void main(String[] args)
    {
        SparkSession spark= SparkSession
                .builder()
                .appName("Sql2Es")
                .config("spark.driver.host","192.168.1.121")
                .config("es.nodes","192.168.1.231")
                .config("es.port","9200")
                .master("spark://es01:7077")
                .getOrCreate();

        Dataset<Row>  data = JavaEsSparkSQL.esDF(spark, "base_bzxzf");
        data.show();

        Map<String, String> db = new HashMap<String, String>();
        db.put("url", url);
        db.put("driver", driver);
        db.put("user", "aps");
        db.put("password" , "aps");
        db.put("dbtable", "base_bzxzf");

        Dataset<Row> ds = spark.read().format("jdbc").options(db).load();
        ds.show();


        spark.stop();

    }
}
