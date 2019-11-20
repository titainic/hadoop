package spark.ml.bayes;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.nd4j.linalg.factory.Nd4j;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.util.List;

import static org.apache.spark.sql.functions.col;

/**
 * 鸢尾花构建贝叶斯分类
 */
public class NaiveBayesExample2
{
    public static void main(String[] args)
    {
        SparkSession spark = SparkSession
                .builder()
                .master("local")
                .appName("朴素贝叶斯2")
                .getOrCreate();


        Dataset<Row> ds = spark.read().format("csv")
                .option("header", "true")
                .load("/home/titanic/soft/intellij_workspace/github-hadoop/com-spark-ml/src/main/resources/bayes/iris.csv");

        /**
         * 构建模型所需要的数据集，把csv转换成libsvm格式
         */
        //features特征列
        String[] col = {"SepalLength", "SepalWidth"};
        VectorAssembler assembler = new VectorAssembler();
        //选择要vector的列
        assembler.setInputCols(col).setOutputCol("features");
        Dataset<Row> mlds = ds.select(col("SepalLength").cast("double"), col("SepalWidth").cast("double"),col("label").cast("int"));
        Dataset<Row> tmpds = assembler.transform(mlds);
        Dataset<Row> tranDS = tmpds.select(col("label"), col("features"));



        /**
         * 模型训练
         */
        //切分训练数据集和测试数据集
        Dataset<Row>[] splits = tranDS.randomSplit(new double[]{0.6, 0.4}, 1234L);
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



        /**
         * 可视化数据x,y
         */
        List<Double> lengthList = ds.select(col("SepalLength")).map(new MapFunction<Row, Double>()
        {
            public Double call(Row row) throws Exception
            {
                return Double.valueOf(row.getString(0));
            }
        }, Encoders.DOUBLE()).collectAsList();

        List<Double> widthList = ds.select(col("SepalWidth")).map(new MapFunction<Row, Double>()
        {
            public Double call(Row row) throws Exception
            {
                return Double.valueOf(row.getString(0));
            }
        }, Encoders.DOUBLE()).collectAsList();


        spark.stop();


        double[] x = Nd4j.create(lengthList).data().asDouble();
        double[] y = Nd4j.create(widthList).data().asDouble();

        spark.stop();

        //构建可视化
        Layout layout = Layout.builder()
                .title("鸢尾花构建贝叶斯")
                .xAxis(Axis.builder().build())
                .yAxis(Axis.builder().build())
                .build();
        Trace polt = ScatterTrace.builder(x,y).marker(Marker.builder().opacity(.5).build()).build();

        Plot.show(new Figure(layout, polt));
    }


}
