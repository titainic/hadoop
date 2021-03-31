package com.titanic.flink.example;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;


public class DataSetWordCount {

    public static void main(String[] args) throws Exception {
//        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> readDS = env.readTextFile("com-flink/src/main/resources/hello.txt");

        readDS.print();

        env.execute();

    }
}
