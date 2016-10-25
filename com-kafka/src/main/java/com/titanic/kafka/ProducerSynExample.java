package com.titanic.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * 生产者同步生产数据
 */
public class ProducerSynExample
{
    public static void main(String[] args)
    {
        producer();
    }

    private static void producer()
    {
        long events = Long.MAX_VALUE;
        Random rnd = new Random();
        Properties props = new Properties();
        props.put("metadata.broker.list", "127.0.0.1:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //kafka.serializer.DefaultEncoder
        props.put("partitioner.class", "kafka.producer.partiton.SimplePartitioner");
        //kafka.producer.DefaultPartitioner: based on the hash of the key
        //props.put("request.required.acks", "1");
        props.put("producer.type", "async");
        //props.put("producer.type", "1");
        // 1: async 2: sync

        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<String, String>(config);

        for (long nEvents = 0; nEvents < events; nEvents++)
        {
            long runtime = new Date().getTime();
            String ip = "127.0.0.1";
            String msg = runtime + ",www.example.com," + ip;
            KeyedMessage<String, String> data = new KeyedMessage<String, String>("titanic", ip, msg);
            producer.send(data);
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        producer.close();
    }
}
