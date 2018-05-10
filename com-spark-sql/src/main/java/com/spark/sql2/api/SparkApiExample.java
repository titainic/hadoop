package com.spark.sql2.api;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.spark.sql.functions.lit;

/**
 * spark sql DataSet　API
 */
public class SparkApiExample
{
    public static String spark_master = "spark://192.9.7.68:7077";
    public static String user = "root";
    public static String pwd = "Tyky@123";
    public static String ip = "192.9.7.91:3306";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "csx";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&amp;characterEncoding=UTF-8&amp;allowMultiQueries=true&rewriteBatchedStatements=true&useSSL=false";

    public static void main(String[] args)
    {
        init();
    }

    public static void init()
    {
        String resultTime = getTime();

        SparkSession spark = SparkSession.builder().
                master(spark_master).
                appName("  " + resultTime).
                getOrCreate();

        spark.sparkContext().addJar("/home/titanic/soft/intellij_workspace/github-hadoop/com-spark-sql/target/com-spark-sql-0.0.1-SNAPSHOT.jar");


        Map<String, String> FundsPeopleMap = new HashMap<String, String>();
        FundsPeopleMap.put("url", url);
        FundsPeopleMap.put("driver", driver);
        FundsPeopleMap.put("dbtable", "funds_people");

        Dataset<Row> FundsPeopleDS = spark.read().format("jdbc").options(FundsPeopleMap).load();


        //新增列，并给列赋值
        Dataset<Row> data = FundsPeopleDS.withColumn("result_date", lit(resultTime));
        data.show();

        //当前内存中释放这个ＤＳ
        FundsPeopleDS.unpersist();

        spark.stop();

    }

    /**
     * 当前时间
     * @return
     */
    public static String getTime()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(date);
        return dateNowStr;

    }
}
