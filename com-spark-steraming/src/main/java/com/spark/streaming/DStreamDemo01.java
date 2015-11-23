package com.spark.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by wb-yangbin.d on 2015/11/23.
 */
public class DStreamDemo01
{
    public static void main(String[] rgs)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("DStream");
        JavaStreamingContext jscc = new JavaStreamingContext(conf, Durations.seconds(1));

        JavaDStream<String> lines = jscc.socketTextStream("localhost",7777);

        JavaDStream<String> errorLines = lines.filter(new Function<String, Boolean>()
        {
            public Boolean call(String s) throws Exception
            {
                return s.contains("error");
            }
        });

        errorLines.print();

        jscc.start();
        jscc.awaitTermination();
    }
}
