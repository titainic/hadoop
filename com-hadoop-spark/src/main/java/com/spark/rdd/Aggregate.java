package com.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义RDD返回类型。不是完全懂
 */
public class Aggregate
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {

        SparkConf conf = new SparkConf();
        conf.setAppName("binend arrregate");
        conf.setMaster("spark://binend:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/WorkSpace_intellij/hadoop-spark/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        List list = new ArrayList();
        JavaRDD rdd = jsc.parallelize(list);


        //通过这个函数把RDD里面的元素合并放入累加器，考虑到每个节点都是在本地进行累加的
        Function2<AvgCount,Integer,AvgCount> addAndCount = new Function2<AvgCount, Integer, AvgCount>()
        {
            public AvgCount call(AvgCount a, Integer x) throws Exception
            {
                a.total += x;
                a.num += x;
                return a;
            }
        };

        //通过这个函数把累加器两辆进行合并
        Function2<AvgCount,AvgCount,AvgCount> combine = new Function2<AvgCount, AvgCount, AvgCount>()
        {
            public AvgCount call(AvgCount a, AvgCount b) throws Exception
            {
                a.total += b.total;
                a.num += b.num;
                return a;
            }
        };

        AvgCount inittial = new AvgCount(10,2);
        AvgCount redult = (AvgCount) rdd.aggregate(inittial,addAndCount,combine);
        System.out.println(redult.avg());

    }


}

class AvgCount implements Serializable
{

    public AvgCount(int total, int num)
    {
        this.total = total;
        this.num = num;
    }

    public int total;
    public int num;


    public double avg()
    {
        return total/num;
    }

}
