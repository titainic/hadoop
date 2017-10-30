package com.spark.sql2;

import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;

/**
 * 养老保险完成
 */
public class YanglaobaoxianSpark
{

    public static String user = "root";
    public static String pwd = "TYKYadmin@258";
    public static String ip = "10.22.55.30:3306";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "hnrescat";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&characterEncoding=UTF8&useCursorFetch=true&defaultFetchSize=100";

    public static void main(String[] args)
    {

        init();
    }

    private static void init()
    {
        SparkSession spark = SparkSession.builder().
                master("spark://10.22.55.28:7077").
                appName("YanglaobaoxianSpark").
                getOrCreate();

//        spark.conf().set("spark.sql.crossJoin.enabled", "true");
        spark.conf().set("spark.network.timeout", "10000000");

        Map<String, String> canbaoParam = new HashMap<String, String>();
        canbaoParam.put("url", url);
        canbaoParam.put("driver", driver);
        canbaoParam.put("dbtable", "canbao_fundscode");

        Map<String, String> fundsParam = new HashMap<String, String>();
        fundsParam.put("url", url);
        fundsParam.put("driver", driver);
        fundsParam.put("dbtable", "funds_people");

        Map<String, String> yanglaocanbaoParam = new HashMap<String, String>();
        yanglaocanbaoParam.put("url", url);
        yanglaocanbaoParam.put("driver", driver);
        yanglaocanbaoParam.put("dbtable", "base_yanglaocanbao");

        Dataset<Row> canbaoDS = spark.read().format("jdbc").options(canbaoParam).load().select(col("funds_code"));

        Dataset<Row> fundsDS = spark.read().format("jdbc").options(fundsParam).load().repartition(64, col("id"));

        Dataset<Row> yanglaocanbaoDS = spark.read().format("jdbc").options(yanglaocanbaoParam).load().select(col("id"),col("sfzhm").as("id_number"));

        List<String> list = canbaoDS.mapPartitions(new MapPartitionsFunction<Row, String>()
        {
            public Iterator<String> call(Iterator<Row> iterator) throws Exception
            {
                List<String> list = new ArrayList<String>();
                while (iterator.hasNext())
                {
                    list.add(iterator.next().mkString());
                }
                return list.iterator();
            }
        }, Encoders.STRING()).collectAsList();

//        System.out.println(list.size()); 132
        Dataset<Row> inDS = fundsDS.select(col("id"),col("id_number")).where(col("funds_code").isin(list.toArray()));
//        System.out.println(inDS.count()); 29491961
        Dataset<Row> resultDS = inDS.except(yanglaocanbaoDS);
//        System.out.println(resultDS.count()); 6384364


        resultDS.createOrReplaceTempView("resultDS");//6384364
        fundsDS.createOrReplaceTempView("fundsDS");//7000w

        Dataset<Row> result = fundsDS.sqlContext().sql("SELECT " +
                "t1.id,"+
//                "t1.funds_code," +
//                "t1.funds_name," +
                "t1.people_name," +
                "t1.id_number" +
//                "t1.time," +
//                "t1.xzqh_code" +
                " FROM fundsDS t1,resultDS t2 " +
                " WHERE t1.id = t2.id").repartition(64);


        result.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "e_funds_people_old_insurance")
                .mode(SaveMode.Append)
                .save();

        spark.stop();

    }
}
