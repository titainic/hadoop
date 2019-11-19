package spark.ml.bayes;

import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * 朴素贝叶斯
 */
public class JavaNaiveBayesExample
{
    public static void main(String[] args)
    {
        SparkSession spark = SparkSession
                .builder()
                .master("spark://titanic:7077")
                .appName("朴素贝叶斯")
                .getOrCreate();

        //加载数据
        Dataset<Row> dataFrame = spark.read().format("libsvm").load("/home/titanic/soft/intellij_workspace/github-hadoop/com-spark-ml/src/main/resources/bayes/sample_libsvm_data.txt");

        //切分训练数据集和测试数据集
        Dataset<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];

        //创建朴素贝叶斯模型
        NaiveBayes nb = new NaiveBayes();

        // 训练模型
        NaiveBayesModel model = nb.fit(train);

        //测试模型
        Dataset<Row> predictions = model.transform(test);
        predictions.show();


        //在测试集上面计算精度
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test set accuracy = " + accuracy);

        spark.stop();
    }
}
