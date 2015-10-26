//package com.spark.rdd;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by titanic on 15-10-26.
// */
//public class Fold
//{
////    public static void main(String[] args)
////    {
////        init();
////    }
//
//    private static void init()
//    {
//
//        SparkConf conf = new SparkConf();
//        conf.setAppName("binend fold");
//        conf.setMaster("spark://binend:7077");
//
//        JavaSparkContext jsc = new JavaSparkContext(conf);
//        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");
//
//        List<Integer> aList = new ArrayList<Integer>();
//
//        for(int i = 0 ; i <= 30; i++)
//        {
//            aList.add(i);
//        }
//
//       JavaRDD<Integer> rdd = jsc.parallelize(aList);
//
//    }
//}
