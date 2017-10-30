package com.spark.sql2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

public class test
{
    public static String user = "root";
    public static String pwd = "12345678";
    public static String ip = "112.5.141.83:9060";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "hnrescat";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&characterEncoding=UTF8";


    public static void main(String[] args)
    {
        SparkSession spark = SparkSession.builder().
                master("spark://titanic:7077").
                appName("FinancialContrastSpark").
                getOrCreate();

        //来源表
        Map<String, String> disabledParam = new HashMap<String, String>();
        disabledParam.put("url", url);
        disabledParam.put("driver", driver);
        disabledParam.put("dbtable", "funds_source");

        Map<String, String> gongParam = new HashMap<String, String>();
        gongParam.put("url", url);
        gongParam.put("driver", driver);
        gongParam.put("dbtable", "funds_gone");
//
        Dataset<Row> gongDS = spark.read().format("jdbc").options(gongParam).load();//.where(col("level").isin("1"));

        Dataset<Row> provinceDS = spark.read().format("jdbc").options(disabledParam).load();//.where(col("level").isin("1"));
        provinceDS.createOrReplaceTempView("funds_source");
        gongDS.createOrReplaceTempView("funds_gone");

//        provinceDS.sqlContext().sql("SELECT id FROM funds_source").show();
        gongDS.sqlContext().sql("SELECT COUNT(s.id) FROM funds_source s, funds_gone g WHERE s.p_funds_code = g.funds_code").show();

        spark.stop();

    }
}
