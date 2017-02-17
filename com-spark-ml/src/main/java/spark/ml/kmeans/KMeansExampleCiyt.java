package spark.ml.kmeans;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * k-means聚类实现
 */
public class KMeansExampleCiyt implements Serializable
{
    public static void main(String[] args)
    {
        KMeansExampleCiyt kmeans = new KMeansExampleCiyt();
        kmeans.startKmeans();

    }

    private void startKmeans()
    {
        String dataPath = "/home/titanic/soft/intellij_work/hadoop/com-spark-ml/src/main/resources/k-means/city.txt";
        SparkSession spark = SparkSession.builder().master("spark://titanic-Lenovo:7077").appName("k-means").getOrCreate();
        SparkContext sc = spark.sparkContext();

        sc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-ml/target/com-spark-ml-0.0.1-SNAPSHOT.jar");
        JavaRDD<String> cityRDD = spark.read().textFile("file://" + dataPath).javaRDD();

        System.out.println(cityRDD.first());

        JavaRDD<Vector> paresdRDD = cityRDD.map(new Function<String, Vector>()
        {
            public Vector call(String s) throws Exception
            {
                String[] sarray = s.split(" ");
                double[] values = new double[sarray.length];
                for (int i = 0; i < sarray.length; i++)
                {
                    values[i] = Double.parseDouble(sarray[i]);
                }
                return Vectors.dense(values);
            }
        });

        paresdRDD.cache();

        int numClusters = 3; //预测分为3个簇类
        int numIterations = 20; //迭代20次
        int runs = 10; //运行10次，选出最优解

        final KMeansModel clusters = KMeans.train(paresdRDD.rdd(), numClusters, numIterations, runs);

        //计算测试数据分别属于那个簇类
        List<String> resList = paresdRDD.map(new Function<Vector, String>()
        {
            public String call(Vector v) throws Exception
            {
                String d = v.toString() + " belong to cluster :" + clusters.predict(v);
                return d;
            }
        }).collect();




        spark.stop();
    }

    public <T> void print(Collection<T> c)
    {
        for (T t : c)
        {
            System.out.println(t.toString());
        }
    }
}
