package com.spark.kafka;


import scala.Tuple2;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;


import java.util.HashSet;


import com.google.common.collect.Lists;

import org.apache.spark.streaming.Durations;


public class Test1_2 {
    private static final Pattern SPACE = Pattern.compile(" ");

    /**
     * Consumes messages from one or more topics in Kafka and does wordcount.
     * Usage: DirectKafkaWordCount <brokers> <topics>
     *   <brokers> is a list of one or more Kafka brokers
     *   <topics> is a list of one or more kafka topics to consume from
     *
     * Example:
     *    $ bin/run-example streaming.KafkaWordCount broker1-host:port,broker2-host:port topic1,topic2
     *
     *    bin/run-example Test1
     *
     *
     */
//    public static void main(String[] args) {
//       init();
//
//    }

    private static void init()
    {
        // TODO Auto-generated method stub

        // Create a local StreamingContext with two working thread and batch interval of 1 second
        //SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
        //JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

        //StreamingExamples.setStreamingLogLevels();

        String brokers = "127.0.0.1:9092";
        String topics = "HJW.T1";
        String group = "HJW.T1";
        String zookeeper = "127.0.0.1:2181";

        // Create context with 2 second batch interval
        SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafkaWordCount");
        sparkConf.setMaster("spark://t1m1.tcloud.com:7077");
        sparkConf.setJars(new String[]{"/home/titanic/soft/WorkSpace_intellij/hadoop-hdfs/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar"});
        //sparkConf.setJars(jars);
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));

        HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
        HashMap<String, String> kafkaParams = new HashMap<String, String>();
        kafkaParams.put("metadata.broker.list", brokers);

        // Create direct kafka stream with brokers and topics
	   /* JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
	        jssc,
	        String.class,
	        String.class,
	        StringDecoder.class,
	        StringDecoder.class,
	        kafkaParams,
	        topicsSet
	    );*/


        String numThread = "2";
        int numThreads = Integer.parseInt(numThread);
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        String[] topics1 = topics.split(",");
        for (String topic : topics1) {
            System.out.println("【"+topic+"】");
            topicMap.put(topic, numThreads);
        }

        JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, zookeeper, group, topicMap);

        // Get the lines, split them into words, count the words and print
        JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {

            public String call(Tuple2<String, String> tuple2) {
                System.out.println("【"+tuple2._2()+"】");
                return tuple2._2();
            }
        });

        int i=0;
        i=1;

        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

            public Iterable<String> call(String x) {
                System.out.println("【"+x+"】");
                return Lists.newArrayList(SPACE.split(x));
            }
        });

        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(
                new PairFunction<String, String, Integer>() {

                    public Tuple2<String, Integer> call(String s) {
                        System.out.println("【"+s+"】");
                        return new Tuple2<String, Integer>(s, 1);
                    }
                }).reduceByKey(new Function2<Integer, Integer, Integer>() {

            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        });

        wordCounts.print();

        // Start the computation
        jssc.start();
        jssc.awaitTermination();

    }

}