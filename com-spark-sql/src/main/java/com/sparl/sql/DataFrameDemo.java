package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;

import java.util.List;

/**
 * spark sql DataFrame第一次操作
 */
public class DataFrameDemo
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setAppName("DataFrame").setMaster("local");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        SQLContext sqlctx = new SQLContext(jsc);

        org.apache.spark.sql.DataFrame df = sqlctx.read().json("D:\\work\\intellij_20151110\\spark\\com-spark-sql\\src\\main\\resources\\testweet.json");

        //把加载的数据注册为零时表，给个表名
        df.registerTempTable("tweets");

        org.apache.spark.sql.DataFrame sqlForTweets = sqlctx.sql("SELECT text, retweetCount FROM tweets ORDER BY retweetCount LIMIT 10");

        List<Row> rowList = sqlForTweets.collectAsList();
        sqlForTweets.show();

        System.out.println(rowList.get(0).toString());

        JavaRDD<String> rdd = sqlForTweets.toJavaRDD().map(new Function<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return row.getString(0);
            }
        });
        List<String> rddList = rdd.collect();

        for(String s : rddList)
        {
            System.out.println(s);
        }

        jsc.stop();

    }
}
