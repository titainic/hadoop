package spark.ml.regression;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

public class LinearRegressionExample
{
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
                .load("file:///home/titanic/soft/intellij_workspace/github-hadoop/com-spark-ml/src/main/resources/LinearRegression/lpsa.data");


//        data.show();
         data.col("_c0").cast(DataTypes.DoubleType);
        data.printSchema();

//        LinearRegression lr = new LinearRegression()
//                .setLabelCol("_c0")
//                .setFeaturesCol("_c1")
//                .setMaxIter(10) // 迭代次数
//                .setRegParam(0.3)
//                .setElasticNetParam(0.8);
//
//        LinearRegressionModel lrModel = lr.fit(data);
//
//        System.out.println("Coefficients: " + lrModel.coefficients() + " Intercept: " + lrModel.intercept());
//
//        LinearRegressionTrainingSummary trainingSummary = lrModel.summary();
//        System.out.println("numIterations: " + trainingSummary.totalIterations());
//        System.out.println("objectiveHistory: " + Vectors.dense(trainingSummary.objectiveHistory()));
//        trainingSummary.residuals().show();
//        System.out.println("RMSE(均方根、标准差): " + trainingSummary.rootMeanSquaredError());
//        System.out.println("R2(决定系数)=: " + trainingSummary.r2());

        spark.stop();
    }


}
