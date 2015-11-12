package com.kafka.demo;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class KafkaProducer
{

    private Producer<String, byte[]> producer;
    private String topic ;
    private Schema schema = null;
    private Schema.Parser parser = null;

    public void init(String tabName){
        Properties props = new Properties();
		/*
		 * 读取配置文件
		 */
        File f = new File(
                System.getProperty("user.dir") + File.separator + "conf" + File.separator + "sysConfig.properties");
        InputStream inputstream = null;
        try {
            inputstream = new FileInputStream(f);
            props.load(inputstream);
            producer = new Producer<String, byte[]>(new ProducerConfig(props));
//            topic = props.getProperty(SysConstants.KAFKA_TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputstream) {
                    inputstream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        parser = new Schema.Parser();
        String filePath = System.getProperty("user.dir") + File.separator + "conf" +File.separator + "avro" + File.separator + tabName +".avsc";
        File schemaFile = new File(filePath);
        try {
            schema = parser.parse(schemaFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void producerArvo(List<Map<String,String>> res,String filePath) throws Exception {
        DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);

        FileOutputStream fis = new FileOutputStream(new File(filePath),true);
        DataFileWriter<GenericRecord> df = new DataFileWriter<GenericRecord>(writer);
        df.create(schema, fis);

        int x = 0;
        if(res.size()>0){
            for (Map<String, String> map : res) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                GenericRecord datum = new GenericData.Record(schema);
                Set<String> set = map.keySet();
                System.out.println(map.toString());
                for (String string : set) {
                    datum.put(string.toLowerCase(), map.get(string));
                }
                df.append(datum);
            }
            df.close();
            producer.close();
            System.out.println("process sucessful");
        }
    }

    public void producer(List<Map<String,String>> res)throws Exception{

        DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);

        int x = 0;
        if(res.size()>0){
            for (Map<String, String> map : res) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                GenericRecord datum = new GenericData.Record(schema);
                Set<String> set = map.keySet();
                System.out.println(map.toString());
                for (String string : set) {
                    datum.put(string.toLowerCase(), map.get(string));
                }
                writer.write(datum, encoder);
                encoder.flush();
                x++;
                byte[] byteData = out.toByteArray();

                KeyedMessage<String, byte[]> data = new KeyedMessage<String, byte[]>(topic, x+"", byteData);

                producer.send(data);
                System.out.println("计数器: " + x);
            }
            producer.close();
            System.out.println("process sucessful");
        }
    }
}