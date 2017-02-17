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
public class KMeansExample implements Serializable
{
    public static void main(String[] args)
    {
        KMeansExample kmeans = new KMeansExample();
        kmeans.startKmeans();

    }

    private void startKmeans()
    {
        String dataPath = "/home/titanic/soft/intellij_work/hadoop/com-spark-ml/src/main/resources/k-means/data.txt";
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


        print(resList);
        System.out.println("-----------------------");

        //计算cost
        double wssse = clusters.computeCost(paresdRDD.rdd());
        System.out.println("Within Set Sum of Squared Errors = " + wssse);

        System.out.println("-----------------------");
        //打印出中心点
        System.out.println("Cluster centers:");
        for (Vector center : clusters.clusterCenters())
        {
            System.out.println(" " + center);
        }


        System.out.println("-----------------------");

        //进行一些预测
        System.out.println("Prediction of (1.1, 2.1, 3.1): "
                + clusters.predict(Vectors.dense(new double[]{1.1, 2.1, 3.1})));
        System.out.println("Prediction of (10.1, 9.1, 11.1): "
                + clusters.predict(Vectors.dense(new double[]{10.1, 9.1, 11.1})));
        System.out.println("Prediction of (21.1, 17.1, 16.1): "
                + clusters.predict(Vectors.dense(new double[]{21.1, 17.1, 16.1})));


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
