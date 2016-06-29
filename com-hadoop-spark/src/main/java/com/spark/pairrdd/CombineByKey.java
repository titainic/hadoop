package com.spark.pairrdd;

import akka.japi.Function2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 自定义RDD返回类型
 */
public class CombineByKey
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("CombineByKey");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List<Tuple2<Integer,Integer>> list = new ArrayList<Tuple2<Integer,Integer>>();
        for(int x = 0 ; x <= 10 ; x++)
        {
            Tuple2 tuple2 = new Tuple2(x,x);
            list.add(tuple2);
        }
        JavaPairRDD<Integer,Integer> nums = jsc.parallelizePairs(list);


        Function<Integer,AvgCount> createAcc = new Function<Integer, AvgCount>()
        {
            public AvgCount apply(Integer x)
            {
                return new AvgCount(x,1);
            }
        };
        Function2<AvgCount,Integer,AvgCount> addAndCount = new Function2<AvgCount, Integer, AvgCount>()
        {
            public AvgCount apply(AvgCount avgCount, Integer x) throws Exception
            {
                avgCount.total += x;
                avgCount.num += x;
                return avgCount;
            }
        };

        Function2<AvgCount,AvgCount,AvgCount> combine = new Function2<AvgCount, AvgCount, AvgCount>()
        {
            public AvgCount apply(AvgCount avgCount, AvgCount avgCount2) throws Exception
            {
                avgCount.total += avgCount2.total;
                avgCount.num += avgCount2.num;
                return avgCount;
            }
        };

        AvgCount inital = new AvgCount(0,0);

        //这个地方报错了。以后有时间在研究。
//        JavaPairRDD<String,AvgCount> avgCount = nums.combineByKey(createAcc, addAndCount, combine);
//        Map<String,AvgCount> map = avgCount.collectAsMap();
//
//        for(Map.Entry<String,AvgCount> entry : map.entrySet())
//        {
//            System.out.println(entry.getKey()+entry.getValue().avg());
//        }

    }

    public static class AvgCount implements Serializable
    {
        public AvgCount(int total, int num)
        {
            this.total = total;
            this.num = num;
        }

        public int total;
        public int num;

        public float avg()
        {
            return total/num;
        }

    }
}


