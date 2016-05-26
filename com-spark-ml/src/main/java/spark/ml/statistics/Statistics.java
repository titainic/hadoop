package spark.ml.statistics;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;

import java.util.ArrayList;
import java.util.List;

/**
 *  MLlib Statistics是基础统计模块，对RDD格式数据进行统计
 */
public class Statistics
{

    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf sc = new SparkConf();
        sc.setAppName("Statistics");
        sc.setMaster("spark://titanic-Lenovo:7077");

        JavaSparkContext jsc = new JavaSparkContext(sc);
        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-ml/target/com-spark-ml-0.0.1-SNAPSHOT.jar");

        //resources下面的数据文件
        JavaRDD<String> data = jsc.textFile("file:///home/titanic/soft/intellij_work/hadoop/com-spark-ml/src/main/resources/sample_stat.txt");
        JavaRDD<Integer> data1 = data.flatMap(new FlatMapFunction<String, Integer>()
        {
            public Iterable<Integer> call(String s) throws Exception
            {
                String[] arrs = s.split(",");
                List<Integer> list = new ArrayList<Integer>();
                for(String str : arrs)
                {
                    System.out.println(str);
                    list.add(Integer.valueOf(str));
                }
                return list;
            }
        });

        JavaRDD<Vector> vdata =  data1.map(new Function<Integer, Vector>()
        {
            public Vector call(Integer integer) throws Exception
            {
                Vector v = Vectors.dense(integer);
                return v;
            }
        });

        MultivariateStatisticalSummary mss = org.apache.spark.mllib.stat.Statistics.colStats(vdata.rdd());


        System.out.println(mss.max());
        System.out.println(mss.min());
        System.out.println(mss.mean());
        System.out.println(mss.variance());
        System.out.println(mss.normL1());
        System.out.println(mss.normL2());
    }
}
