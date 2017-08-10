package com.titanic.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * kafka消费者模型：分区消费模型，组消费模型。两种消费模型
 * <p>
 * 组消费模型
 * 测试通过
 */
public class ConsumerGroupExample
{

    public static String zk = "192.9.4.9:2181,192.9.4.10:2181,192.9.4.11:2181";

    public static String groupId = "titanic_g";

    public static String topic = "test2";

    public static String consumerid = "titcnia_c";

    public static void main(String[] args)
    {
        groupConsumer();
    }



    /**
     * 组消费模型
     */
    public static void groupConsumer()
    {

        Properties props = new Properties();
        props.put("group.id", groupId);
        props.put("consumer.id", consumerid);
        props.put("zookeeper.connect", zk);
        props.put("zookeeper.session.timeout.ms", "60000");
        props.put("zookeeper.sync.time.ms", "2000");
        // props.put("auto.commit.interval.ms", "1000");

        ConsumerConfig config = new ConsumerConfig(props);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(config);

        // 设置每个topic开几个线程
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        // 获取stream
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);

        List<KafkaStream<byte[], byte[]>> streamsList = consumerMap.get(topic);
        ExecutorService executor = Executors.newFixedThreadPool(1);

        for (KafkaStream stream : streamsList)
        {
            executor.execute(new MyStreamThread(stream));
        }
    }

    public static class MyStreamThread implements Runnable
    {
        private KafkaStream<byte[], byte[]> stream;

        public MyStreamThread(KafkaStream<byte[], byte[]> stream)
        {
            this.stream = stream;
        }

        public void run()
        {
            System.out.println("...运行");
            ConsumerIterator<byte[], byte[]> streamIterator = stream.iterator();
            while (streamIterator.hasNext())
            {
                MessageAndMetadata<byte[], byte[]> message = streamIterator.next();
                String topic = message.topic();
                int partition = message.partition();
                long offset = message.offset();
//                String key = new String(message.key());
                String msg = new String(message.message());
                // 在这里处理消息,这里仅简单的输出
                // 如果消息消费失败，可以将已上信息打印到日志中，活着发送到报警短信和邮件中，以便后续处理
                System.out.println("consumerid:" + consumerid + ", thread : " + Thread.currentThread().getName()
                        + ", topic : " + topic + ", partition : " + partition + ", offset : " + offset + " , key : "
                         + " , mess : " + msg);
            }
        }
    }


}
