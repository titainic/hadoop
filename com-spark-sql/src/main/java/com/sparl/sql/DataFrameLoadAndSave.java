package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

/**
 * DataFrame加载文件和存储文件
 */
public class DataFrameLoadAndSave
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("DataFrameLoadAndSave");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sqlc = new SQLContext(jsc);

        DataFrame df = sqlc.read().format("json").json("D:\\work\\intellij_20151110\\spark\\com-spark-sql\\src\\main\\resources\\people.json");
        df.select("name","age").write().format("parquet").save("path");
    }
}
