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
 * 　当我们有小文件时，这一切都很好，但是我希望它也能够传输大文件。
 * 卡夫卡的设计并不是为了移动大的信息，它也不是移动像照片这样的大型二进制文件的最佳选择。
 * 在她的文章中，Kafka Gwen Shapira提出了一些处理大消息的方法，并说卡夫卡的最佳消息大小大约是10k。
 * 因此，我决定去寻找分裂的解决方案，并把我发送给10k块的文件分割给kafka。
 *
 * 使用者必须将文件从其块中组合起来。我在这里选择的简单方法是创建一个只有一个分区的主题。
 * 通过这种方式，我可以确保这些块以相同的顺序到达消费者。这在一个非常繁忙的生产系统中是不具有规模的，
 * 而且可能还不够。使用并行发布和消费的多个分区将会更加健壮，但是在消费者端需要更复杂的逻辑，
 * 以便能够从非有序的块中组装文件。为了演示，我将坚持更简单的解决方案。
 *
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
//        int i;
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

        //监控文件变化
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
