package com.spark.sql2;

import org.apache.spark.deploy.SparkSubmit;

public class SparkSubmitMain
{
    public static void main(String[] args)
    {
        String[] param = new String[] { "--master", "spark://127.0.0.1:7077",
                                        "--name", "java Submit Spark",
                                        "--class", "com.hunan.spark.test",
//                                        "--num-executors","8",
//                                        "--driver-memory","2g",
//                                        "executor-memory","25g",
//                                        "--executor-cores","4",
                                        "/home/titanic/soft/intellij_work/hunan/hunan-spark-contrast/target/hunan-spark-1.jar"};
        SparkSubmit.main(param);
    }
}
