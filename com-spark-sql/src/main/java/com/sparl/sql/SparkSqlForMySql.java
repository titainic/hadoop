//package com.sparl.sql;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.sql.DataFrame;
//import org.apache.spark.sql.SQLContext;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * spark链接mysql数据库 spark1.6版本
// */
//public class SparkSqlForMySql
//{
//
//
////    public static void main(String[] rgs)
////    {
////        init();
////    }
//
//    private static void init()
//    {
//        SparkConf conf = new SparkConf().setMaster("local").setAppName("SparkSqlForMySql");
//        JavaSparkContext jsc = new JavaSparkContext(conf);
//        SQLContext sqlctx = new SQLContext(jsc);
//
//        Map<String, String> jdbcUrlParam = new HashMap<String, String>();
//        jdbcUrlParam.clear();
//        jdbcUrlParam.put("url","jdbc:mysql://10.101.235.37:3306/spark?user=spark&password=123456&useUnicode=true&characterEncoding=UTF8");
//        jdbcUrlParam.put("driver","com.mysql.jdbc.Driver");
//        jdbcUrlParam.put("dbtable", "spark_info_demo");
//
//        DataFrame jdbcMysqlDF = sqlctx.read().format("jdbc").options(jdbcUrlParam).load();
//
//        jdbcMysqlDF.show();
//
//        jdbcMysqlDF.select(jdbcMysqlDF.col("id"),jdbcMysqlDF.col("name"),jdbcMysqlDF.col("gender"),jdbcMysqlDF.col("address")).show();
//
//        jsc.stop();
//
//
//    }
//
//
//}
