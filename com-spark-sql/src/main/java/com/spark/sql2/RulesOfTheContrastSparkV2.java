package com.spark.sql2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;

/**
 * 规则对比具体spark实现
 * 个体户，房产，死亡人口直接取2交集
 *
 */
public class RulesOfTheContrastSparkV2
{
    public static String user = "root";
    public static String pwd = "tyky@123";
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
                master("spark://59.231.9.98:7077").
                appName("RulesOfTheContrast").
                getOrCreate();

//        spark.conf().set("spark.sql.crossJoin.enabled", "true");
        spark.conf().set("spark.network.timeout", "10000000");

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

//        Dataset<Row> disabledDS = spark.read().format("jdbc").options(disabledParam).load().select(col("sfzhm").as("id_number"));

//        Dataset<Row> fupinDS = spark.read().format("jdbc").options(fupinParam).load().select(col("zjhm").as("id_number"));

//        Dataset<Row> fangchanDS = spark.read().format("jdbc").options(fangchanParam).load().select(col("sfzhm").as("id_number"));

        Dataset<Row> gthDS = spark.read().format("jdbc").options(gthParam).load().select(col("sfzhm").as("id_number"));

//        Dataset<Row> swrkDS = spark.read().format("jdbc").options(swrkParam).load().select(col("sfzhm").as("id_number"));

        Dataset<Row> fundsDS = spark.read().format("jdbc").options(funddsParam).load().select(col("id_number")).repartition(64);


//        fundsDS.except(disabledDS);
//        Dataset<Row> fangchanResultDS = fundsDS.intersect(fangchanDS);

        Dataset<Row> gthResultDS = fundsDS.intersect(gthDS);
        List<Row> gthList = gthResultDS.collectAsList();
        List<String> gthArrList = new ArrayList<String>();
        for (Row r : gthList)
        {
            gthArrList.add(r.mkString());
        }

        Dataset<Row> ResultgthDS = fundsDS.where(col("id_number").isin(gthArrList.toArray()));
        System.out.println(ResultgthDS.count());


/****************************************************************************/
//
//        //房产
//        fangchanResultDS.write()
//                .format("jdbc")
//                .option("url", url)
//                .option("dbtable", "r_fangchan")
//                .mode(SaveMode.Append)
//                .save();


/*************************************************/
//        //死亡人口
//        Dataset<Row> swrkResultDS = fundsDS.intersect(swrkDS);
//        List<Row> swrklist = swrkResultDS.collectAsList();
//        List<String> arrswrkList = new ArrayList<String>();
//        for (Row r : swrklist)
//        {
//            arrswrkList.add(r.mkString());
//        }
//        Dataset<Row> swrkResult = fundsDS.where(col("id_number").isin(arrswrkList.toArray()));
//        //死亡人口
//        swrkResult.write()
//                .format("jdbc")
//                .option("url", url)
//                .option("dbtable", "e_funds_people_death")
//                .mode(SaveMode.Append)
//                .save();

        spark.stop();

    }

}
