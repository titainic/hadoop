package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 把相同key的元素进行分组
 */
public class GroupByKey
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("groupByKey");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> rdd = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md");
        JavaRDD<String> rddx = rdd.flatMap(new FlatMapFunction<String, String>()
        {
            public Iterable<String> call(String s) throws Exception
            {
                return Arrays.asList(s.split(" "));
            }
        });

        JavaRDD<String> rddc = rddx.map(new Function<String, String>()
        {
            public String call(String s) throws Exception
            {
                String regEx = "[`~!@#$%^&*()+=|{}';',.<>?~！@#￥%……&*（）+|{}【】‘；：”“’。，、？]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(s);
                return m.replaceAll("").trim();
            }
        });

        String debugString = rddc.toDebugString();
        System.out.println(debugString);

                                                                             //输入的值，输出的k， 输出的v
        JavaPairRDD<String,Integer> groupByKey = rddc.mapToPair(new PairFunction<String, String, Integer>()
        {
            public Tuple2<String, Integer> call(String s) throws Exception
            {
                return new Tuple2<String, Integer>(s,1);
            }

        });

        JavaPairRDD<String, Iterable<Integer>> gRDD = groupByKey.groupByKey();

        Map<String, Iterable<Integer>> gmap = gRDD.collectAsMap();

        System.out.println(gmap);


    }
}
