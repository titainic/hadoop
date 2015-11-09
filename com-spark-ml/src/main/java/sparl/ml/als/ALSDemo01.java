package sparl.ml.als;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;

/**
 * Created by titanic on 15-11-7.
 */
public class ALSDemo01
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setMaster("spark://localhost:7077");
        conf.setAppName("ALS demo 01");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-spark-ml/target/com-spark-ml-0.0.1-SNAPSHOT.jar");

        JavaRDD<String> dataRDD = jsc.textFile("file:///home/titanic/soft/Work_Intellij/20151106/com-spark-ml/src/main/resources/u.data");

        JavaRDD<String[]> rawRatings = dataRDD.map(new Function<String, String[]>()
        {
            public String[] call(String s) throws Exception
            {
                String[] ss = s.split("\t");
                String[] xx = new String[]{ss[0], ss[1], ss[2]};
                return xx;
            }
        });
//        String[] raw = rawRatings.first();
//        for(String s : raw)
//        {
//            System.out.println(s);
//        }
          JavaRDD<Rating> ratings = rawRatings.map(new Function<String[], Rating>()
          {
              public Rating call(String[] strings) throws Exception
              {
                  return new Rating(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Double.parseDouble(strings[2]));
              }
          });


        //对应ALS模型中因子个数
        int rank = 50;

        //运行时的迭代次数
        int numIterations = 10;

        //控制模型的正则化过程
        double lambda = 0.01;
        MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), rank, numIterations, lambda);
//         =  JavaPairRDD.fromJavaRDD(model.userFeatures());




    }
}
