package com.spark2.dataset;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;

/**
 * DataSet中isin使用
 */
public class DataSetColIsin
{
    public static String user = "root";
    public static String pwd = "Tyky@123";
    public static String ip = "192.9.7.91:3306";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "tyky";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&amp;characterEncoding=UTF-8&amp;allowMultiQueries=true&rewriteBatchedStatements=true&useSSL=false";


    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {

        SparkSession spark = SparkSession.builder().
                master("spark://tyky00:7077").
                appName("jjj").
                getOrCreate();

        spark.sparkContext().addJar("/home/titanic/soft/intellij_worksparce/github-hadoop/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");



        Map<String, String> data = new HashMap<String, String>();
        data.put("url", url);
        data.put("driver", driver);
        data.put("dbtable", "o_domain");

        Map<String, String> isin = new HashMap<String, String>();
        isin.put("url", url);
        isin.put("driver", driver);
        isin.put("dbtable", "o_domain_x");


        Dataset<Row> dataDS = spark.read().format("jdbc").options(data).load();
        Dataset<Row> isinDS = spark.read().format("jdbc").options(isin).load().select(col("a_id").as("remark"));


        List<String> list = isinDS.map(new MapFunction<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return row.mkString();
            }
        }, Encoders.STRING()).collectAsList();

                                                                //这里注意，isin的使用
        Dataset<Row> dax =  dataDS.where(col("remark").isin(list.toArray()));
        dax.show();
        spark.stop();
    }
}
