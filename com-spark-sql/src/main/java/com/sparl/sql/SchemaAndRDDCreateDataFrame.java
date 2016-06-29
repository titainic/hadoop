package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema和RDD创建DataFrame
 */
public class SchemaAndRDDCreateDataFrame
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("SchemaAndRDDCreateDataFrame");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sqlc = new SQLContext(jsc);

        JavaRDD<String> peopleRDD = jsc.textFile("file:///D:\\work\\intellij_20151110\\spark\\com-spark-sql\\src\\main\\resources\\people.txt");
        String schemaString = "name age";

        List<StructField> fields = new ArrayList<StructField>();
        for(String fieldName : schemaString.split(" "))
        {
            fields.add(DataTypes.createStructField(fieldName,DataTypes.StringType,true));
        }

        StructType schema = DataTypes.createStructType(fields);

        JavaRDD<Row> rowRDD = peopleRDD.map(new Function<String, Row>()
        {
            public Row call(String s) throws Exception
            {
                String[] fields = s.split(",");

                return RowFactory.create(fields[0],fields[1].trim());
            }
        });

        DataFrame peopleDataFrame = sqlc.createDataFrame(rowRDD,schema);

        peopleDataFrame.registerTempTable("people");

        DataFrame resule = sqlc.sql("SELECT name FROM people");

        List<String> names = resule.toJavaRDD().map(new Function<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return "Name:" + row.getString(0);
            }
        }).collect();

        for (String s : names)
        {
            System.out.println(s);
        }


    }
}
