package sparl.ml.als;

import org.apache.spark.SparkConf;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.*;

/**
 *最畅销的产品
 */
public class JavaApp
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("Java APP");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-spark-ml/target/com-spark-ml-0.0.1-SNAPSHOT.jar");

        JavaRDD<String> cvsRDD  = jsc.textFile("file:///home/titanic/soft/Work_Intellij/20151106/com-spark-ml/src/main/resources/UserPurchaseHistory.csv");
        JavaRDD<String[]> data = cvsRDD.map(new Function<String, String[]>()
        {
            public String[] call(String s) throws Exception
            {
                return s.split(",");
            }
        });

        //求总购买次数
        long numPurchases = data.count();

        //有多少个不同的客户购买过商品
        JavaRDD<String> comumerRDD = data.map(new Function<String[], String>()
        {
            public String call(String[] strings) throws Exception
            {
                return strings[0];
            }
        });
        //有多少个不同的客户
        long uniqueUser = comumerRDD.distinct().count();

        //求和得出总收入
        JavaDoubleRDD countMoney = data.mapToDouble(new DoubleFunction<String[]>()
        {
            public double call(String[] strings) throws Exception
            {
                return Double.parseDouble(strings[2]);
            }
        });
        double totalRevenus = countMoney.sum();

        //求最畅销的产品是哪个
        //首先用一个PairFunction和Tuple2类将数据映射成（product，1）格式记录
        //然后，用一个Function2类调用reduceByKey操作，该操作实际上是一个求和操作
        JavaPairRDD<String,Integer> pairsRDD = data.mapToPair(new PairFunction<String[], String, Integer>()
        {
            public Tuple2<String, Integer> call(String[] strings) throws Exception
            {
                return new Tuple2<String, Integer>(strings[1],1);
            }
        });

        JavaPairRDD<String,Integer> reduceRDD =  pairsRDD.reduceByKey(new Function2<Integer, Integer, Integer>()
        {
            public Integer call(Integer integer, Integer integer2) throws Exception
            {
                return integer + integer2;
            }
        });

        List<Tuple2<String,Integer>> pairs =  reduceRDD.collect();

        //最后对结果进行排序。注意，这里会需要创建一个comparator函数来进行降序排列
        Collections.sort(pairs, new Comparator<Tuple2<String, Integer>>()
        {
            public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2)
            {
                return -(o1._2 - o2._2);
            }
        });

        String mostPopular = pairs.get(0)._1();
        int purchases = pairs.get(0)._2();
        System.out.println("总购买 : " + numPurchases);
        System.out.println("独立用户 : " + uniqueUser);
        System.out.println("总收入 : " + totalRevenus);
        System.out.println(String.format("最受欢迎的产品 : %s 有 %d 个购买",mostPopular,purchases));
    }
}
