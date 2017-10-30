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
 * 残疾人，完成
 */
public class DisabledSpark
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
                appName("RulesOfTheContrast").
                getOrCreate();

//        spark.conf().set("spark.sql.crossJoin.enabled", "true");
        spark.conf().set("spark.network.timeout", "10000000");

        Map<String, String> canjiParam = new HashMap<String, String>();
        canjiParam.put("url", url);
        canjiParam.put("driver", driver);
        canjiParam.put("dbtable", "canjibt_fundscode");

        Map<String, String> fundsParam = new HashMap<String, String>();
        fundsParam.put("url", url);
        fundsParam.put("driver", driver);
        fundsParam.put("dbtable", "funds_people");

        Map<String, String> disableParam = new HashMap<String, String>();
        disableParam.put("url", url);
        disableParam.put("driver", driver);
        disableParam.put("dbtable", "base_disabled");

        Dataset<Row> canjiDS = spark.read().format("jdbc").options(canjiParam).load().select(col("funds_code"));
        Dataset<Row> fundsDS = spark.read().format("jdbc").options(fundsParam).load().repartition(64, col("id"));
        Dataset<Row> disableDS = spark.read().format("jdbc").options(disableParam).load().select(col("sfzhm").as("id_number"));

        List<String> canList = canjiDS.map(new MapFunction<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return row.mkString();
            }
        },Encoders.STRING()).collectAsList();



        Dataset<Row> codeDS = fundsDS.where(col("funds_code").isin(canList.toArray()));
//        System.out.println(codeDS.count()+"codeDS.count()");

        Dataset<Row> idNumber = fundsDS.select("id_number");
        Dataset<Row> ridNmber =  idNumber.except(disableDS);
//        System.out.println(ridNmber.count()+"codeDS.count()");

        codeDS.createOrReplaceTempView("codeDS");
        ridNmber.createOrReplaceTempView("ridNmber");

        Dataset<Row> resultIsInDS = codeDS.sqlContext().sql("SELECT " +
                "t1.id AS p_id," +
                "t1.funds_code," +
                "t1.funds_name," +
                "t1.people_name," +
                "t1.id_number," +
                "t1.time," +
                "t1.money," +
                "t1.xzqh_code " +
                "FROM codeDS t1,ridNmber t2 WHERE t1.id_number = t2.id_number");

        resultIsInDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_disabled")
                .mode(SaveMode.Append)
                .save();
        spark.stop();

    }
}
