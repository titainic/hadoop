package com.titanic.flink.example;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;

public class StreamWordCount {

    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createRemoteEnvironment("172.18.0.3", 6123);
        DataStream<String> readStream = env.readTextFile("src/main/resources/hello.txt");

        DataStream<Row> data = readStream.flatMap(new FlatMapFunction<String, Row>() {
            @Override
            public void flatMap(String s, Collector<Row> collector) throws Exception {
                Row row = Row.of(s.split(" "));
                collector.collect(row);
            }
        });

        data.print();
        env.execute();
    }
}
