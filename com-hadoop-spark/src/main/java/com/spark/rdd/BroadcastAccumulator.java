//package com.spark.rdd;
//
//import org.apache.spark.Accumulator;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.api.java.function.Function2;
//import org.apache.spark.api.java.function.PairFunction;
//import org.apache.spark.broadcast.Broadcast;
//import org.apache.spark.streaming.Durations;
//import org.apache.spark.streaming.Time;
//import org.apache.spark.streaming.api.java.JavaPairDStream;
//import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;
//import scala.Tuple2;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * 广播变量和累加器
// */
//public class BroadcastAccumulator
//{
//    /**
//     * 肯定要创建一个广播List
//     * <p>
//     * 在上下文中实例化！
//     */
//    private static volatile Broadcast<List<String>> broadcastList = null;
//
//    /**
//     * 计数器！
//     * 在上下文中实例化！
//     */
//    private static volatile Accumulator<Integer> accumulator = null;
//
//    public static void main(String[] args)
//    {
//
//        SparkConf conf = new SparkConf().setMaster("local[2]").
//                setAppName("WordCountOnlieBroadcast");
//
//        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));
//
//
//        /**
//         * 没有action的话，广播并不会发出去！
//         *
//         * 使用broadcast广播黑名单到每个Executor中！
//         */
//        broadcastList = jsc.sc().broadcast(Arrays.asList("Hadoop", "Mahout", "Hive"));
//
//        /**
//         * 全局计数器！用于统计在线过滤了多少个黑名单！
//         */
//        accumulator = jsc.sparkContext().accumulator(0, "OnlineBlackListCounter");
//
//
//        JavaReceiverInputDStream<String> lines = jsc.socketTextStream("Master", 9999);
//
//
//        /**
//         * 这里省去flatmap因为名单是一个个的！
//         */
//        JavaPairDStream<String, Integer> pairs = lines.mapToPair(new PairFunction<String, String, Integer>()
//        {
//            public Tuple2<String, Integer> call(String word)
//            {
//                return new Tuple2<String, Integer>(word, 1);
//            }
//        });
//
//        JavaPairDStream<String, Integer> wordsCount = pairs.reduceByKey(new Function2<Integer, Integer, Integer>()
//        {
//            public Integer call(Integer v1, Integer v2)
//            {
//                return v1 + v2;
//            }
//        });
//
//        /**
//         * Funtion里面 前几个参数是 入参。
//         * 后面的出参。
//         * 体现在call方法里面！
//         *
//         * 这里直接基于RDD进行操作了！
//         */
//        wordsCount.foreach(new Function2<JavaPairRDD<String, Integer>, Time, Void>()
//        {
//            public Void call(JavaPairRDD<String, Integer> rdd, Time time) throws Exception
//            {
//                rdd.filter(new Function<Tuple2<String, Integer>, Boolean>()
//                {
//                    public Boolean call(Tuple2<String, Integer> wordPair) throws Exception
//                    {
//                        if (broadcastList.value().contains(wordPair._1))
//                        {
//
//                            /**
//                             * accumulator不应该仅仅用来计数。
//                             * 可以同时写进数据库或者redis中！
//                             */
//                            accumulator.add(wordPair._2);
//                            return false;
//                        } else
//                        {
//                            return true;
//                        }
//                    }
//
//                    ;
//                    /**
//                     * 这里真的希望 广播和计数器执行的话。要进行一个action操作！
//                     */
//                }).collect();
//
//                System.out.println("广播器里面的值" + broadcastList.value());
//                System.out.println("计时器里面的值" + accumulator.value());
//                return null;
//            }
//        });
//
//
//        jsc.start();
//        jsc.awaitTermination();
//        jsc.close();
//
//    }
//}
