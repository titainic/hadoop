package com.spark.sql2;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;

public class GthSpark
{
    public static String user = "root";
    public static String pwd = "TYKYadmin@258";
    public static String ip = "10.22.55.30:3306";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "hnrescat";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&characterEncoding=UTF8";

    public static void main(String[] args)
    {

        init();
    }

    private static void init()
    {
        SparkSession spark = SparkSession.builder().
                master("spark://10.22.55.28:7077").
                appName("GthSpark").
                getOrCreate();

        spark.conf().set("spark.network.timeout", "10000000");

        //个体户转换DS
        Map<String, String> gthParam = new HashMap<String, String>();
        gthParam.put("url", url);
        gthParam.put("driver", driver);
        gthParam.put("dbtable", "base_gth");

        //对比
        Map<String, String> funddsParam = new HashMap<String, String>();
        funddsParam.put("url", url);
        funddsParam.put("driver", driver);
        funddsParam.put("dbtable", "funds_people");

        Map<String, String> provinceParam = new HashMap<String, String>();
        provinceParam.put("url", url);
        provinceParam.put("driver", driver);
        provinceParam.put("dbtable", "sys_funds_type_province");

        Dataset<Row> provinceDS = spark.read().format("jdbc").options(provinceParam).load().select(col("funds_code"));
        Dataset<Row> gthDS = spark.read().format("jdbc").options(gthParam).load();
        Dataset<Row> fundsDS = spark.read().format("jdbc").options(funddsParam).load();

        List<String> list = provinceDS.map(new MapFunction<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return row.mkString();
            }
        }, Encoders.STRING()).collectAsList();

        Dataset<Row> fundsWhereDS = fundsDS.where(col("funds_code").isin(list.toArray()));

        fundsWhereDS.createOrReplaceTempView("fundsWhereDS");
        gthDS.createOrReplaceTempView("gthDS");

        Dataset<Row> resultDS =  gthDS.sqlContext().sql("SELECT " +
                "t1.id AS p_id," +
                "t1.funds_code," +
                "t1.funds_name," +
                "t1.people_name," +
                "t1.money," +
                "t1.xzqh_code," +
                "t1.id_number" +
                " FROM fundsWhereDS t1,gthDS t2 WHERE t1.id_number = t2.sfzhm");

        resultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_private_business")
                .mode(SaveMode.Append)
                .save();

        spark.stop();
    }
}
