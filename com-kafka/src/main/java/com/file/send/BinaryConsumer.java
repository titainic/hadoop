package com.file.send;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * 文件接收
 */
public class BinaryConsumer
{
    String topic = "filestopic";
    Properties props;
    KafkaConsumer<String, byte[]> consumer;
    String outDir = "/home/titanic/soft/kafka/outdir";


    public BinaryConsumer()
    {
        props = new Properties();
        props.put("bootstrap.servers", "192.9.4.9:9092,192.9.4.10:9092,192.9.4.11:9092");
        props.put("group.id","testx");
        props.put("enable.auto.commit", "true");
        props.put("compression.type", "snappy");
        props.put("fetch.message.max.bytes", "7340032");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
    }

    public void createConsumer()
    {
        consumer = new KafkaConsumer<String, byte[]>(props);
        consumer.subscribe(Arrays.asList(topic));
    }

    public void start() throws IOException
    {
        String name = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while (true)
        {
            ConsumerRecords<String, byte[]> records = consumer.poll(100);
            for (ConsumerRecord<String, byte[]> record : records)
            {
                if (record.value() == null)
                {
                    System.out.println("Writing file " + name);
                    writeFile(name, bos.toByteArray());
                    bos.reset();
                } else
                {
                    name = record.key();
                    bos.write(record.value());
                }
            }
        }
    }

    private void writeFile(String name, byte[] rawData) throws IOException
    {
        File f = new File(outDir);
        if (!f.exists())
        {
            f.mkdir();
        }

        FileOutputStream fos = new FileOutputStream(outDir + File.separator + name);
        fos.write(rawData);
        fos.flush();
        fos.close();
    }

    public static void main(String[] args)
    {
        try
        {
            BinaryConsumer abc;
            abc = new BinaryConsumer();
            abc.createConsumer();
            abc.start();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }
}
