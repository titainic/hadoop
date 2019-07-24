package spark.ml.regression;

import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import static org.apache.spark.sql.functions.col;

public class LinearRegressionExample
{
   public static String dataPath = LinearRegressionExample.class.getClassLoader().getResource("LinearRegression/lpsa.data").getFile();
    public static String SPARK_HOME = "spark://titanic:7077";

    public static void main(String[] args)
    {
        SparkSession spark = SparkSession.builder().
                master(SPARK_HOME).
                appName("LinearRegressionExample").
                getOrCreate();

        Dataset<Row> data =  spark.read()
//                .option("header", "true")
//                .option("inferSchema","true")
                .format("csv")
                .load(dataPath);


//        data.show();
        Dataset<Row> data1 =data.select(col("_c0").cast(DataTypes.DoubleType),col("_c1").cast(DataTypes.DoubleType));
        data1.printSchema();

        LinearRegression lr = new LinearRegression()
                .setLabelCol("_c0")
                .setFeaturesCol("_c1")
                .setMaxIter(10) // 迭代次数
                .setRegParam(0.3)
                .setElasticNetParam(0.8);

        LinearRegressionModel lrModel = lr.fit(data);

        System.out.println("Coefficients: " + lrModel.coefficients() + " Intercept: " + lrModel.intercept());

        LinearRegressionTrainingSummary trainingSummary = lrModel.summary();
        System.out.println("numIterations: " + trainingSummary.totalIterations());
        System.out.println("objectiveHistory: " + Vectors.dense(trainingSummary.objectiveHistory()));
        trainingSummary.residuals().show();
        trainingSummary.meanSquaredError();
        System.out.println("RMSE(均方根、标准差): " + trainingSummary.rootMeanSquaredError());
        System.out.println("R2(决定系数)=: " + trainingSummary.r2());

        spark.stop();
    }


}
