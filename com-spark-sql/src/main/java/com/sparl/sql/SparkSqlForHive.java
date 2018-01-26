package com.sparl.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;

/**
 *  在intellij中使用spark以yarn模式提交到集群。链接hive,
 *  version:
 *          hadoop3.0.0
 *          spark2.2.1
 *          hive2.3
 *
 *          在intellij运行中设置environment variables 设置SPARK_HOME
 */
public class SparkSqlForHive
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf().
                setMaster("yarn").
                setAppName("SparkSqlForHive");

        //链接hive使用
        System.setProperty("hive.metastore.uris", "thrift://tyky01:9083");

        //intellij 使用yarn模式提交任务到集群
//        System.setProperty("hadoop.home.dir","/opt/soft/hadoop-3.0.0");
//        System.setProperty("HADOOP_CONF_DIR", "/opt/soft/spark-2.2.1-bin-hadoop2.7/conf/spark-env.sh");

//        conf.set("spark.yarn.preserve.staging.files","false");
        conf.set("spark.hadoop.yarn.resourcemanager.hostname", "tyky00");
        conf.set("spark.hadoop.yarn.resourcemanager.address", "tyky00:8032");

//        conf.set("spark.yarn.archive", "hdfs://tyky00:8020/spark/spark-jars.zip");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/yarn-site.xml"));
        jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/core-site.xml"));
        jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/hdfs-site.xml"));
        jsc.hadoopConfiguration().addResource(SparkSqlForHive.class.getClassLoader().getResourceAsStream("conf/mapred-site.xml"));
        jsc.addJar("/home/titanic/soft/intellij_worksparce/github-hadoop/com-spark-sql/target/com-spark-sql-0.0.1-SNAPSHOT.jar");

        HiveContext sqlContext = new HiveContext(jsc.sc());
        Dataset<Row> df = sqlContext.sql("select * from default.o_opendata_hive");
        df.show();
        jsc.stop();
    }

}
