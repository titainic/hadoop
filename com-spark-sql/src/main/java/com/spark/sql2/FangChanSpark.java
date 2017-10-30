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

/**
 * 房产
 * 功能完成
 */
public class FangChanSpark
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
                appName("FangChanSpark").
                getOrCreate();

        spark.conf().set("spark.network.timeout", "10000000");

        //房产转换DS
        Map<String, String> fangchanParam = new HashMap<String, String>();
        fangchanParam.put("url", url);
        fangchanParam.put("driver", driver);
        fangchanParam.put("dbtable", "base_fangchan");

        //房产转换DS
        Map<String, String> provinceParam = new HashMap<String, String>();
        provinceParam.put("url", url);
        provinceParam.put("driver", driver);
        provinceParam.put("dbtable", "sys_funds_type_province");


        //对比
        Map<String, String> funddsParam = new HashMap<String, String>();
        funddsParam.put("url", url);
        funddsParam.put("driver", driver);
        funddsParam.put("dbtable", "funds_people");

        Dataset<Row> provinceDS = spark.read().format("jdbc").options(provinceParam).load().select(col("funds_code"));
        Dataset<Row> fangchanDS = spark.read().format("jdbc").options(fangchanParam).load().select(col("sfzhm"));
        Dataset<Row> fundsDS = spark.read().format("jdbc").options(funddsParam).load().repartition(64);


        List<String> provinceList = provinceDS.map(new MapFunction<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return row.mkString();
            }
        },Encoders.STRING()).collectAsList();

        List<String> list = fangchanDS.map(new MapFunction<Row, String>()
        {
            public String call(Row row) throws Exception
            {

                return row.mkString();
            }
        }, Encoders.STRING()).collectAsList();

        Dataset<Row> fancDS = fundsDS.where(col("funds_code").isin(provinceList.toArray()));

        Dataset<Row> result = fancDS.where(col("id_number").isin(list.toArray()));

        result.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_house")
                .mode(SaveMode.Append)
                .save();

        spark.stop();
    }
}
