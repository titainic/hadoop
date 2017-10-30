package com.spark.sql2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

import static org.apache.spark.sql.functions.col;

/**
 * 公安有车数据异常对比
 */
public class GAErrorSpark
{


    public static String user = "root";
    public static String pwd = "TYKYadmin@258";
    public static String ip = "10.22.55.30:3306";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "hnrescat";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&characterEncoding=UTF8&amp;allowMultiQueries=true&rewriteBatchedStatements=true";

    public static void main(String[] args)
    {

        init();
    }

    private static void init()
    {
        SparkSession spark = SparkSession.builder().
                master("spark://10.22.55.28:7077").
                appName("GAErrorSpark").
                getOrCreate();

        spark.conf().set("spark.network.timeout", "10000000");

        Map<String, String> cheParam = new HashMap<String, String>();
        cheParam.put("url", url);
        cheParam.put("driver", driver);
        cheParam.put("dbtable", "result_youche");

        Map<String, String> fundsParam = new HashMap<String, String>();
        fundsParam.put("url", url);
        fundsParam.put("driver", driver);
        fundsParam.put("dbtable", "funds_people");

        Dataset<Row> cheDS = spark.read().format("jdbc").options(cheParam).load().repartition(64);
        Dataset<Row> fundsDS = spark.read().format("jdbc").options(fundsParam).load().repartition(200,col("id"));

        cheDS.createOrReplaceTempView("result_youche");
        fundsDS.createOrReplaceTempView("funds_people");

        Dataset<Row> resultDS = fundsDS.sqlContext().sql("SELECT t1.id AS p_id," +
                                                                        "t1.money," +
                                                                        "t2.cllx," +
                                                                        "t2.xm," +
                                                                        "t2.sfzhm," +
                                                                        "t2.cldjsj " +
                                                                "FROM funds_people t1,result_youche t2 " +
                                                                "WHERE t1.id_number=t2.sfzhm ");
        resultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_car")
                .mode(SaveMode.Append)
                .save();

        spark.stop();
    }
}
