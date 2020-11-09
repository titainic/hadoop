package com.sparl.sql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

public class SparkSqlForOracle
{
    public static String user = "system";
    public static String pwd = "oracle";
    public static String ip = "192.168.1.105:1521";
    public static String driver = "oracle.jdbc.OracleDriver";
    public static String db = "helowin";
    public static String url = "jdbc:oracle:thin:@" + ip + ":" + db  ;

    //2338935 2349654 2467812 2823795 9510141 9632736 9676194
    public static void main(String[] args)
    {
        SparkSession spark = SparkSession.builder().
                master("spark://titanic:7077").
                appName("SparkLoadOracle").
                getOrCreate();

        System.out.println(url);
        Map<String, String> canjiParam = new HashMap<String, String>();
        canjiParam.put("url", url);
        canjiParam.put("user", user);
        canjiParam.put("password", pwd);
        canjiParam.put("driver", driver);
        canjiParam.put("dbtable", "UP_PRO_MATERIAL_V4");

        Dataset<Row> dataDS = spark.read().format("jdbc").options(canjiParam).load();

//        System.out.println(dataDS.count());

//        dataDS.repartition(1).write().mode("Append").csv("file:///home/titanic/soft/spark_save/data");

        spark.stop();


    }

}
