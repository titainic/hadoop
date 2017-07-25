package com.titanic.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by titanic on 17-7-19.
 */
public class ConsumerGroupExample2
{
    public static void main(String[] args)
    {
        Properties pro = new Properties();
        pro.put("bootstrap.servers", "192.9.4.9:9092,192.9.4.10:9092,192.9.4.11:9092");
        pro.put("group.id", "test");
        pro.put("enable.auto.commit", "true");
        pro.put("auto.commit.inteval.ms", "1000");
        pro.put("session.timeout.ms", "30000");
        pro.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        pro.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(pro);


        //订阅多个topic,"test2"和"test3"
//        consumer.subscribe(Arrays.asList("test2","test3"));
        consumer.subscribe(Arrays.asList("test2"));

        try
        {
            while (true)
            {
                ConsumerRecords<String,String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records)
                {
                    System.out.printf("offset = %d,key = %s,value = %s\n",record.offset(),record.key(),record.value());
                }
            }
        }finally
        {
            consumer.close();
        }
    }


}
