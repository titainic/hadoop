package com.spark.graphx;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.graphx.EdgeRDD;
import org.apache.spark.graphx.Graph;
import org.apache.spark.graphx.GraphLoader;
import org.apache.spark.graphx.impl.EdgeRDDImpl;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wb-yangbin.d on 2015/11/12.
 */
public class SparkGraphx1
{
    public static void main(String[] args)
    {
        init();
    }

    /**
     * 自定义点的RDD类，基础RDD，自定义边的RDD基础
     *
     *
     * class vRDD eextends RDD
     * {
     *     Long VertexId  点的ID
     *     String name ;  名字
     *     String xuessheng 学生
     * }
     *
     * 然后 sc.parallelize(Array((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
     (5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
     这样实例化 。得到  ，VertexRDD 点RDD
     类似的edgeRDD也是这样
     *
     *
     *
     *
     *
     *
     *
     */
    private static void init()
    {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("graphx");

        JavaSparkContext jsc = new JavaSparkContext(conf);

//        VertexRDD vertexRDD = new VertexRDD()
        List<Tuple2<Integer,Integer>> list = new ArrayList<Tuple2<Integer, Integer>>();
        list.add(new Tuple2<Integer, Integer>(1,1));
        list.add(new Tuple2<Integer, Integer>(2,2));
        list.add(new Tuple2<Integer, Integer>(3,3));
        list.add(new Tuple2<Integer, Integer>(4,4));
        list.add(new Tuple2<Integer, Integer>(5,5));
        JavaPairRDD<Integer,Integer> rdd = jsc.parallelizePairs(list);

        Graph<Object, Object> graph =  GraphLoader.edgeListFile(jsc.sc(), "", true, 1, StorageLevel.MEMORY_ONLY_2(), StorageLevel.MEMORY_ONLY_2());


    }
}
