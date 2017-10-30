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

public class FarenSpark
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
                appName("FarenSpark").
                getOrCreate();

        spark.conf().set("spark.network.timeout", "10000000");

        //个体户转换DS
        Map<String, String> farenParam = new HashMap<String, String>();
        farenParam.put("url", url);
        farenParam.put("driver", driver);
        farenParam.put("dbtable", "base_faren");

        Map<String, String> provinceParam = new HashMap<String, String>();
        provinceParam.put("url", url);
        provinceParam.put("driver", driver);
        provinceParam.put("dbtable", "sys_funds_type_province");

        Map<String, String> funddsParam = new HashMap<String, String>();
        funddsParam.put("url", url);
        funddsParam.put("driver", driver);
        funddsParam.put("dbtable", "funds_people");

        Dataset<Row> sysDS = spark.read().format("jdbc").options(provinceParam).load().select(col("funds_code"));
        Dataset<Row> fundsDS = spark.read().format("jdbc").options(funddsParam).load();
        Dataset<Row> farenDS = spark.read().format("jdbc").options(farenParam).load();

        List<String> list = sysDS.map(new MapFunction<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return row.mkString();
            }
        },Encoders.STRING()).collectAsList();


        Dataset<Row> fundsWhereDS = fundsDS.where(col("funds_code").isin(list.toArray()));

        fundsWhereDS.createOrReplaceTempView("fundsWhereDS");
        farenDS.createOrReplaceTempView("farenDS");

        Dataset<Row> resultDS = farenDS.sqlContext().sql("SELECT " +
                "t1.id AS p_id," +
                "t1.xzqh_code," +
                "t1.funds_code," +
                "t1.funds_name," +
                "t1.people_name," +
                "t1.money," +
                "t1.id_number," +
                "t2.JGZWMC" +
                " FROM fundsWhereDS t1,farenDS t2 WHERE t1.id_number = t2.FDDBRSFZJHM");

        resultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_company")
                .mode(SaveMode.Append)
                .save();
        spark.stop();
    }
}
