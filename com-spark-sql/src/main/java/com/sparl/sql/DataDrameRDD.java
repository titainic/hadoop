package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

/**
 * 官网demo
 */
public class DataDrameRDD
{
//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("DataDrameRDD");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sqlctx = new SQLContext(jsc);

        JavaRDD<Person> people = jsc.textFile("file:///D:\\work\\intellij_20151110\\spark\\com-spark-sql\\src\\main\\resources\\people.txt").map(new Function<String, Person>()
        {
            public Person call(String s) throws Exception
            {
                String[] parts = s.split(",");

                Person person = new Person();
                person.setName(parts[0]);
                person.setAge(Integer.parseInt(parts[1].trim()));
                return person;
            }
        });

        //把RDD转换成DataFrame
        DataFrame schemaPeople = sqlctx.createDataFrame(people,Person.class);

        //注册DataFrame里面的临时表，表名为：person
        schemaPeople.registerTempTable("people");

        DataFrame teenagers = sqlctx.sql("SELECT name FROM people WHERE age >= 13 AND age <= 19");

        teenagers.show();
        List<String> list = teenagers.javaRDD().map(new Function<Row, String>()
        {
            public String call(Row row) throws Exception
            {
                return "Name" + row.getString(0);
            }
        }).collect();

        for (String s : list)
        {
            System.out.println(s);
        }




    }
}
