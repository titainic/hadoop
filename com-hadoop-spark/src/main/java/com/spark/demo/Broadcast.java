package com.spark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;


/**
 * Spark广播
 * 广播变量：广播变量可以在每台机器上缓存只读变量而不需要为各个任务发送该变量的拷贝。他们可以让大的输入数据集的集群拷贝中的节点更加高效
 */
public class Broadcast
{
    public static void main(String[] args)
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("Broadcast");
        conf.setMaster("spark://t1m1.tcloud.com:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-hdfs/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        org.apache.spark.broadcast.Broadcast<String> broadcast = jsc.broadcast("titanic");

        System.out.println(broadcast.value());
    }
}
