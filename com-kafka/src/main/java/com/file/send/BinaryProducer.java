package com.file.send;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * 　文件发送
 */
public class BinaryProducer
{
    Producer<String, byte[]> producer;
    String topic, boostrapServer, watchDir;
    Path path;
    ByteArrayOutputStream out;

    public BinaryProducer() throws IOException
    {

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.9.4.9:9092,192.9.4.10:9092,192.9.4.11:9092");
        props.put("acks", "1");
        props.put("compression.type", "snappy");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        producer = new KafkaProducer<String, byte[]>(props);

        topic = "filestopic";
        watchDir = "/home/titanic/soft/kafka/watchdir";
        path = FileSystems.getDefault().getPath(watchDir);
    }

    /**
     * 拆分文件
     *
     * @param name
     * @param datum
     * @return
     */
    private List<byte[]> splitFile(String name, byte[] datum)
    {
        int i;
        int length = datum.length;

        //10kb大小
        int block = 10240;

        //计算拆分之后文件的快数
        int numblocks = length / block;

        //计数器
        int counter = 0;

        //总大小
        int totalSize = 0;

        int marker = 0;
        byte[] chunk;

        List<byte[]> data = new ArrayList<byte[]>();

        for (int j = 0; j < numblocks; j++)
        {
            counter++;
            chunk = Arrays.copyOfRange(datum, marker, marker + block);
            data.add(chunk);
            totalSize = chunk.length;
            marker += block;
        }
        chunk = Arrays.copyOfRange(datum, marker, length);
        data.add(chunk);
        data.add(null);
        return data;
    }

    private void start() throws IOException, InterruptedException
    {
        String fileName;
        byte[] fileData;
        List<byte[]> allChunks;
        WatchService watcher = FileSystems.getDefault().newWatchService();
        WatchKey key;
        path.register(watcher, ENTRY_CREATE);

        while (true)
        {
            key = watcher.take();
            for (WatchEvent<?> event : key.pollEvents())
            {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == ENTRY_CREATE)
                {
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    fileName = filename.toString();
                    Thread.sleep(500);

                    fileData = Files.readAllBytes(FileSystems.getDefault().getPath(watchDir + File.separator + fileName));
                    allChunks = splitFile(fileName, fileData);

                    for (int i = 0; i < allChunks.size(); i++)
                    {
                        publishMessage(fileName, (allChunks.get(i)));
                    }
                    System.out.println("Published file " + fileName);
                }
            }
            key.reset();
        }
    }

    private void publishMessage(String key, byte[] bytes)
    {
        ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(topic, key, bytes);
        producer.send(data);
    }


    public static void main(String[] args)
    {
        BinaryProducer abp;
        try
        {
            abp = new BinaryProducer();
            abp.start();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
