package com.spark.sql2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

import static org.apache.spark.sql.functions.col;

/**
 * 死亡人口
 * 功能完成
 */
public class SwrkSparkX1
{


    public static String user = "root";
    public static String pwd = "TYKYadmin@258";
    public static String ip = "10.22.55.30:3306";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "hnrescat";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&amp;characterEncoding=UTF-8&amp;allowMultiQueries=true&rewriteBatchedStatements=true";

    public static void main(String[] args)
    {

        init();
    }

    private static void init()
    {
        SparkSession spark = SparkSession.builder().
                master("spark://10.22.55.28:7077").
                appName("SwrkSparkX1").
                getOrCreate();

        spark.conf().set("spark.network.timeout", "10000000");
        spark.conf().set("spark.debug.maxToStringFields","1000");

        //死亡人口转换DS
        Map<String, String> swrkParam = new HashMap<String, String>();
        swrkParam.put("url", url);
        swrkParam.put("driver", driver);
        swrkParam.put("dbtable", "base_swrk");

        //对比
        Map<String, String> funddsParam = new HashMap<String, String>();
        funddsParam.put("url", url);
        funddsParam.put("driver", driver);
        funddsParam.put("dbtable", "funds_people");


        Dataset<Row> swrkDS = spark.read().format("jdbc").options(swrkParam).load().where(col("is_valid").isin("1")).repartition(64);

        Dataset<Row> fundsDS = spark.read().format("jdbc").options(funddsParam).load().repartition(64,col("id"),col("time"),col("money"));
        swrkDS.createOrReplaceTempView("base_swrk");
        fundsDS.createOrReplaceTempView("funds_people");

        Dataset<Row> unResult = fundsDS.sqlContext().sql("SELECT t1.id AS p_id," +
                                                                        "t1.time, " +
                                                                        "t1.xzqh_code," +
                                                                        "t2.qssj " +
                                                                "FROM funds_people t1,base_swrk t2 WHERE t1.id_number = t2.sfzhm ").coalesce(64);

        Dataset<Row> resultDSTmp = unResult.select(col("p_id"),col("xzqh_code")).where(col("time").gt(col("qssj"))).coalesce(64);

        resultDSTmp.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_death_x")
                .mode(SaveMode.Append)
                .save();

        spark.stop();
    }
}
