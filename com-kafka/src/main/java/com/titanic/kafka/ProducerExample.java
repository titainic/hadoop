package com.titanic.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 生产者生产数据
 *
 * 测试通过
 */
public class ProducerExample
{

    String topic = "titanic";

    KafkaProducer<String, String> producer;

    Properties kafkaProps = new Properties();

    String kafkaServer = "192.9.4.9:9092,192.9.4.10:9092,192.9.4.11:9092";

    public static void main(String[] args)
    {
        ProducerExample example = new ProducerExample();
        example.configure();
        example.start();


        int x = 0;
        while (true)
        {
            System.out.println(x);
            example.produceSync("x:"+x);
            x++;
        }
    }

    public void configure()
    {
        kafkaProps.put("bootstrap.servers", kafkaServer);

        // This is mandatory, even though we don't send keys
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("acks", "1");

        // how many times to retry when produce request fails?
        kafkaProps.put("retries", "3");
        kafkaProps.put("linger.ms", 5);
    }

    public void start()
    {
        producer = new KafkaProducer<String, String>(kafkaProps);
    }

    /**
     * 同步发送
     *
     * @param value
     */
    private void produceSync(String value)
    {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);
        try
        {
            producer.send(record).get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 异步发送
     *
     * @param value
     */
    private void produceAsync(String value)
    {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);
        producer.send(record, new ProducerCallback());
    }

    private class ProducerCallback implements Callback
    {

        public void onCompletion(RecordMetadata recordMetadata, Exception e)
        {
            if (e != null)
            {
                System.out.println("Error producing to topic " + recordMetadata.topic());
                e.printStackTrace();
            }
        }
    }
}
