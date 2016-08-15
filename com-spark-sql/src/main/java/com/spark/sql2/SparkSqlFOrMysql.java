package com.spark.sql2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

/**
 * spark2.0链接mysql
 */
public class SparkSqlFOrMysql
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkSession spark = SparkSession.builder().
                master("spark://titanic-Lenovo:7077").
                appName("spark for mysql").
                getOrCreate();

        Map<String, String> param = new HashMap<String, String>();
        param.put("url", "jdbc:mysql://127.0.0.1:3306/dzzz?user=root&password=a&useUnicode=true&characterEncoding=UTF8");
        param.put("driver", "com.mysql.jdbc.Driver");
        param.put("dbtable", "lic_task");

        Dataset<Row> mysqlds = spark.read().format("jdbc").options(param).load();

        mysqlds.printSchema();


        mysqlds.show();


        spark.stop();
    }


}
