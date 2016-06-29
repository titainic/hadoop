package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

/**
 * DataFrame官网练习
 */
public class DataFrameQuery
{
//    public static void main(String[] args)
//    {
//        DataFrameQuery dfq = new DataFrameQuery();
//        dfq.init();
//    }

    private  void init()
    {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("DataFrameQuery");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sqlctx = new SQLContext(jsc);

        DataFrame df = sqlctx.read().json("D:\\work\\intellij_20151110\\spark\\com-spark-sql\\src\\main\\resources\\people.json");
        //显示结果
        System.out.println("打印结果");
        df.show();

        //打印头信息（表的列名）
        System.out.println("打印列名");
        df.printSchema();

        //只显示列名为name的结果
        System.out.println("只显示列名为name的结果");
        df.select("name").show();

        //查询，把年龄递增 加 1
        System.out.println("查询，把年龄递增 加 1");
        df.select(df.col("name"),df.col("age").plus(1)).show();

        //查询年龄大于21岁的人
        System.out.println("查询年龄大于21岁的人");
        df.filter(df.col("age").gt(21)).show();

        //统计年龄
        System.out.println("统计年龄");
        df.groupBy("age").count().show();
    }
}
