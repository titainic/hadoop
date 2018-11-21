package com.titanic.spark2x.es6x;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.spark.sql.api.java.JavaEsSparkSQL;

public class SparkMain
{
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



        spark.stop();

    }
}
