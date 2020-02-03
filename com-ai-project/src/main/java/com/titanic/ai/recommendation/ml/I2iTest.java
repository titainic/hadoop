package com.titanic.ai.recommendation.ml;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;

public class I2iTest
{
    public static String path = "";


    public static void main(String[] args)
    {
        SparkSession spark = SparkSession
                .builder()
                .master("yarn")
                .appName("协同过滤")
                .config("yarn.resourcemanager.hostname","binend01")
                .config("spark.executor.instance","3")
                .config("spark.yarn.queue","spark")
                .config("spark.yarn.queue","spark")
                .config("spark.driver.host","10.168.2.177")
                .getOrCreate();


        System.out.println(path);
        Dataset<Row> itemConf = spark.read().option("delimiter", ",").option("header", "true").format("csv").load("hdfs://binend01:8020/movies.csv");
        Dataset<Row> userRating = spark.read().option("delimiter", ",").option("header", "true").format("csv").load("hdfs://binend01:8020/ratings.csv");

        List<Row> idTitleList = itemConf.select(col("movieId"),col("title")).collectAsList();
        List<Row> itemId2genresList = itemConf.select(col("movieId"),col("genres")).collectAsList();

        Map<String, String> itemId2titleMap = row2map(idTitleList);
        Map<String, String> itemId2genresMap = row2map(itemId2genresList);

        Broadcast<Map<String, String>> broadcastPlayList = JavaSparkContext.fromSparkContext(spark.sparkContext()).broadcast(itemId2titleMap);


        System.out.println(broadcastPlayList);

        System.out.println(itemId2titleMap);
        System.out.println(itemId2genresMap);

        userRating.show();

        spark.stop();


    }

    public static Map<String,String> row2map(List<Row> list)
    {
        Map<String, String> map = new HashMap<String, String>();
        for (Row r : list)
        {
            map.put(r.get(0) + "", r.get(1) + "");
        }
        return map;
    }
}
