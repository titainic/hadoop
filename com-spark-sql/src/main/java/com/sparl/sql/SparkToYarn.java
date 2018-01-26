package com.sparl.sql;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.List;

public class SparkToYarn {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("yarn").setAppName("SparkYarnJob");
		conf.set("spark.hadoop.yarn.resourcemanager.scheduler.address", "tyky00:8030");
		conf.set("spark.hadoop.yarn.resourcemanager.address", "tyky00:8032");
		conf.set("spark.hadoop.yarn.resourcemanager.hostname", "tyky00");
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(conf);
		jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/yarn-site.xml"));
		jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/core-site.xml"));
		jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/hdfs-site.xml"));
		jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/mapred-site.xml"));
		jsc.addJar("/home/titanic/soft/intellij_worksparce/github-hadoop/com-spark-sql/target/com-spark-sql-0.0.1-SNAPSHOT.jar");
		List<String> listA = new ArrayList<String>();
		listA.add("a");
		listA.add("a");
		listA.add("b");
		listA.add("b");
		listA.add("b");
		listA.add("c");
		listA.add("d");
		JavaRDD<String> rdd = jsc.parallelize(listA);
		JavaPairRDD<Integer, Iterable<String>> pariRDD = rdd.groupBy(new Function<String, Integer>() {

			private static final long serialVersionUID = 4355787344674259584L;

			public Integer call(String s) throws Exception {
				return 1;
			}
		});
		System.out.println(pariRDD.collect());
		System.exit(0);
	}
}