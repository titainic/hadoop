package com.dl4j.spark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.writable.Writable;
import org.datavec.spark.transform.misc.StringToWritablesFunction;
import org.deeplearning4j.spark.datavec.DataVecDataSetFunction;
import org.nd4j.linalg.dataset.DataSet;

import java.util.List;

public class LoadData
{
    public static void main(String[] args)
    {
        String filePath = "hdfs://titanic:8020/dl4j/point1.csv";
        SparkConf sparkConf = new SparkConf().setAppName("LoadData").setMaster("local[*]");
//        sparkConf.set("spark.hadoop.fs.defaultFS", "hdfs://titanic:8020");
        sparkConf.set("spark.kryo.registrator", "org.nd4j.Nd4jRegistrator");

        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        JavaRDD<String> rddString = jsc.textFile(filePath);
        RecordReader recordReader = new CSVRecordReader(',');
        JavaRDD<List<Writable>> rddWritables = rddString.map(new StringToWritablesFunction(recordReader));

        int labelIndex = 2;         //Labels: a single integer representing the class index in column number 5
        int numLabelClasses = 2;   //10 classes for the label
        JavaRDD<DataSet> rdd = rddWritables.map(new DataVecDataSetFunction(labelIndex, numLabelClasses, false));

        List<DataSet> list = rdd.top(1);

        for (int i = 0; i < list.size(); i++)
        {
            System.out.println(list.get(i));
        }

        jsc.stop();


    }
}
