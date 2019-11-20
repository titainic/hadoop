package spark.ml.bayes;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;

public class NaiveBayesExample2
{
    public static void main(String[] args)
    {
        SparkSession spark = SparkSession
                .builder()
                .master("spark://titanic:7077")
                .appName("朴素贝叶斯2")
                .getOrCreate();

        Dataset<Row> ds = spark.read().format("com.databricks.spark.csv")
                .option("delimiter", "\t")
                .option("inferSchema", "true")
                .option("header", "true")
                .load("file:///home/titanic/soft/intellij_workspace/github-hadoop/com-spark-ml/src/main/resources/bayes/train.tsv");
//        segments.printSchema();
        ds.select("html_ratio").printSchema();
        ds.select("image_ratio").printSchema();

        Dataset<Double> htmlList = ds.select(col("html_ratio")).map(new MapFunction<Row, Double>()
        {
            public Double call(Row row) throws Exception
            {
                return row.getDouble(0);
            }
        }, Encoders.DOUBLE());

        htmlList.show();
//        for (int i = 0; i < htmlList.size(); i++)
//        {
//            System.out.println(htmlList.get(i));
//        }
        spark.stop();
    }
}
