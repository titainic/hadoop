package com.sparl.sql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.spark.JavaHBaseContext;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;
import scala.Tuple3;

import java.util.List;

/**
 *Spark链接Hbase
 *
 * 示例代码
 * https://sparkkb.wordpress.com/2015/05/05/read-hbase-table-data-and-create-sql-dataframe-using-spark-api-java-code/
 *http://www.programcreek.com/java-api-examples/index.php?source_dir=learning-hadoop-master/spark-on-hbase/src/test/java/spark/hbase/JavaHBaseContextSuite.java
 * https://www.codatlas.com/github.com/apache/hbase/HEAD/hbase-spark/src/main/java/org/apache/hadoop/hbase/spark/example/hbasecontext/JavaHBaseDistributedScan.java
 *
 * 有异常，解决办法
 * http://stackoverflow.com/questions/33503749/hive-spark-error-java-lang-illegalstateexception-unread-block-data
 *
 * 要把　hbase/lib 下的hbase-protocol-1.2.1.jar,hbase-common-1.2.1.jar,
 *                  htrace-core-3.1.0-incubating.jar
                    hbase-server-1.2.1.jar
                    hbase-client-1.2.1.jar
 hive/lib/
            hive-hbase-handler-1.2.1.jar
            hive-common-2.0.0.jar
            hive-exec-2.0.0.jar

 和hbase-spark-2.0.0-20160316.173537-2.jar
 添加到 spark-env.sh中
 　　　　export SPARK_CLASSPATH=/usr/local/hbase-1.1.2/lib/hbase-protocol-1.1.2.jar:/usr/local/hbase-1.1.2/lib/hbase-common-1.1.2.jar:/usr/local/hbase-1.1.2/lib/htrace-core-3.1.0-incubating.jar:/usr/local/hbase-1.1.2/lib/hbase-server-1.1.2.jar:/usr/local/hbase-1.1.2/lib/hbase-client-1.1.2.jar:/usr/local/hive-1.2.1/lib/hive-hbase-handler-1.2.1.jar:/usr/local/hive-1.2.1/lib/hive-common-1.2.1.jar:/usr/local/hive-1.2.1/lib/hive-exec-1.2.1.jar
 */
public class SparkSqlForHbase
{

//    public static void main(String[] args)
//    {
//        init();
//    }

    private static void init()
    {
        String tableName = "emp";

        SparkConf sc = new SparkConf().setMaster("spark://titanic-Lenovo:7077").setAppName("sparl_hbase");
        JavaSparkContext jsc = new JavaSparkContext(sc);
        jsc.addJar("/home/titanic/soft/intellij_work/hadoop/com-spark-sql/target/com-spark-sql-0.0.1-SNAPSHOT.jar");

        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");

        JavaHBaseContext hBaseContext = new JavaHBaseContext(jsc,conf);
        Scan scan = new Scan();
        scan.setCaching(100);
        JavaRDD<Tuple2<ImmutableBytesWritable, Result>> javaRdd = hBaseContext.hbaseRDD(TableName.valueOf(tableName), scan);


        List<String> results = javaRdd.map(new Function<Tuple2<ImmutableBytesWritable,Result>, String>()
        {
            public String call(Tuple2<ImmutableBytesWritable, Result> v1) throws Exception
            {
                return  Bytes.toString(v1._1().copyBytes());
            }
        }).collect();

        for (String res : results)
        {
            System.out.println(res);
        }
        jsc.stop();
    }

}
