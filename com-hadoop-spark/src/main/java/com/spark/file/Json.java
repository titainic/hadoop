//package com.spark.file;
//
//import org.apache.avro.ipc.specific.Person;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.FlatMapFunction;
//import parquet.org.codehaus.jackson.JsonParser;
//import parquet.org.codehaus.jackson.map.MappingIterator;
//import parquet.org.codehaus.jackson.map.ObjectMapper;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * spark中操作json文件。有问题。带解决
// */
//public class Json
//{
//    public static void main(String[] args)
//    {
//        init();
//    }
//
//    private static void init()
//    {
//        SparkConf conf = new SparkConf();
//        conf.setMaster("local");
//        conf.setAppName("Json");
//
//        JavaSparkContext jsc = new JavaSparkContext(conf);
//
//
//    }
//
//    class ParseJson implements FlatMapFunction<Iterator<String>,Person>
//    {
//        @Override
//        public Iterable<Person> call(Iterator<String> lines) throws Exception
//        {
//            List<Person> list = new ArrayList<Person>();
//            ObjectMapper mapper = new ObjectMapper();
//            while(lines.hasNext())
//            {
//                String line = lines.next();
//                try
//                {
////                    mapper.readValues(line, Person.class);
////                   list.add();
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//
//
//            }
//            return null;
//        }
//    }
//}
