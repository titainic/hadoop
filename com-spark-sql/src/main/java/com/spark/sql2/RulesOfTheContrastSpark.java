package com.spark.sql2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import scala.collection.JavaConversions;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.current_date;

/**
 * 规则对比具体spark实现
 */
public class RulesOfTheContrastSpark
{
    public static String user = "root";
    public static String pwd = "12345678";
    public static String ip = "112.5.141.83:9060";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "yang";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&characterEncoding=UTF8";

    public static void main(String[] args)
    {

        init();
    }

    private static void init()
    {
        SparkSession spark = SparkSession.builder().
                master("spark://titanic:7077").
                appName("spark for mysql").
                getOrCreate();

        spark.conf().set("spark.sql.crossJoin.enabled", "true");

        //残疾人转换DS
        Map<String, String> disabledParam = new HashMap<String, String>();
        disabledParam.put("url", url);
        disabledParam.put("driver", driver);
        disabledParam.put("dbtable", "base_disabled");

        //房产转换DS
        Map<String, String> fangchanParam = new HashMap<String, String>();
        fangchanParam.put("url", url);
        fangchanParam.put("driver", driver);
        fangchanParam.put("dbtable", "base_fangchan");

        //扶贫转换DS
        Map<String, String> fupinParam = new HashMap<String, String>();
        fupinParam.put("url", url);
        fupinParam.put("driver", driver);
        fupinParam.put("dbtable", "base_fupin");

        //个体户转换DS
        Map<String, String> gthParam = new HashMap<String, String>();
        gthParam.put("url", url);
        gthParam.put("driver", driver);
        gthParam.put("dbtable", "base_gth");

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

        Dataset<Row> disabledDS = spark.read().format("jdbc").options(disabledParam).load().select(col("sfzhm").as("id_number"), col("xm").as("people_name"));

        Dataset<Row> fangchanDS = spark.read().format("jdbc").options(fangchanParam).load().select(col("sfzhm").as("id_number"), col("cqr").as("people_name"));

        Dataset<Row> fupinDS = spark.read().format("jdbc").options(fupinParam).load().select(col("zjhm").as("id_number"), col("xm").as("people_name"));

        Dataset<Row> gthDS = spark.read().format("jdbc").options(gthParam).load().select(col("sfzhm").as("id_number"), col("jyz").as("people_name"));

        Dataset<Row> swrkDS = spark.read().format("jdbc").options(swrkParam).load().select(col("sfzhm").as("id_number"), col("qsryxm").as("people_name"));

        Dataset<Row> fundsDS = spark.read().format("jdbc").options(funddsParam).load().select(col("id"),
                col("id_number"),
                col("people_name"),
                col("funds_code"),
                col("sex"),
                col("money"),
                col("time"),
                col("township"),
                col("community"),
                col("small_com"),
                col("remarks"),
                col("create_time"),
                col("create_by"),
                col("update_time"),
                col("update_by"),
                col("xzqh_code"),
                col("grsqze"),
                col("review_by"),
                col("review_type"),
                col("review_time"),
                col("funds_year"),
                col("item_name"),
                current_date());
        Dataset<Row> fundsDateDS = fundsDS.withColumnRenamed("current_date()","count_time");

        //设置检查点
        Dataset<Row> fundsDScheckpoint = fundsDateDS.checkpoint();
        Dataset<Row> disabledDScheckpoint = disabledDS.checkpoint();
        Dataset<Row> fangchanDScheckpoint = fangchanDS.checkpoint();
        Dataset<Row> fupinDScheckpoint = fupinDS.checkpoint();
        Dataset<Row> gthDScheckpoint = gthDS.checkpoint();
        Dataset<Row> swrkDScheckpoint = swrkDS.checkpoint();

        //非残疾人比对结果以身份证号码与姓名对比
        Dataset<Row> disabledResultDS = disabledDScheckpoint.join(fundsDScheckpoint, JavaConversions.asScalaBuffer(asList("id_number", "people_name")));
        disabledResultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_disabled")
                .mode(SaveMode.Append)
                .save();

        //房产比对结果以身份证号码与姓名对比
        Dataset<Row> fangchanResultDS = fangchanDScheckpoint.join(fundsDScheckpoint, JavaConversions.asScalaBuffer(asList("id_number", "people_name")));
        fangchanResultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_house")
                .mode(SaveMode.Append)
                .save();

        //扶贫比对结果以身份证号码与姓名对比
        Dataset<Row> fupinResultDS = fupinDScheckpoint.join(fundsDScheckpoint, JavaConversions.asScalaBuffer(asList("id_number", "people_name")));
        fupinResultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_poor")
                .mode(SaveMode.Append)
                .save();

        //个体户比对结果以身份证号码与姓名对比
        Dataset<Row> gthResultDS = gthDScheckpoint.join(fundsDScheckpoint, JavaConversions.asScalaBuffer(asList("id_number", "people_name")));
        gthResultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_private_business")
                .mode(SaveMode.Append)
                .save();

        //死亡人口比对结果以身份证号码与姓名对比
        Dataset<Row> swrkResultDS = swrkDScheckpoint.join(fundsDScheckpoint, JavaConversions.asScalaBuffer(asList("id_number", "people_name")));
        swrkResultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_death")
                .mode(SaveMode.Append)
                .save();

        spark.stop();
    }

}
