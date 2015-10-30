package com.kafka.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaConsumerRun
{
	private String readKafkaDataAvroFilePath = "E:\\govnet_soft\\data\\data.txt";
	
	private Properties properties = new Properties();
	private ConsumerConfig config = null;
	private ConsumerConnector consumer = null;
	private Schema schema = null;
	
	private FileOutputStream writeStream = null;
	private BufferedWriter write = null;

	
	public static void main(String[] args) throws IOException
	{
		KafkaConsumerRun kRun = new KafkaConsumerRun();
		kRun.initProperties();
		kRun.initKafka();
		kRun.initSchema();
		kRun.runKafka();
	}
	
	/**
	 * �����з���
	 */
	public void runKafka()
	{
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("VIEW_WLW_INFO", new Integer(1));
		Map<String,List<KafkaStream<byte[],byte[]>>> kafkaMap = consumer.createMessageStreams(map);
		List<KafkaStream<byte[],byte[]>> kafkaList = kafkaMap.get("VIEW_WLW_INFO");
		
		long count = 0 ;
		
		for(KafkaStream<byte[], byte[]> kafkaStream : kafkaList)
		{
			ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
			while (it.hasNext())
			{
				count++;
				
				byte[] bytes = it.next().message();
				
				DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
				Decoder avroData = DecoderFactory.get().binaryDecoder(bytes, null);
				GenericRecord result = null;
				try
				{
					result = reader.read(null, avroData);
					writeFile(readKafkaDataAvroFilePath,result.toString());
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
				consumer.commitOffsets();
				
				if(count % 10000 == 0)
				{
					System.out.println("kafka�����"+ count+"�����");
					if(write != null)
					{
						try
						{
							write.close();
							writeStream.close();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				
			}
		}
		
	}
	
	/**
	 * ��kafka��ȡ������ݴ��뵽�����ļ���
	 * @param filrPath
	 * @param strData
	 * @throws IOException
	 */
	private void writeFile(String filrPath,String strData) throws IOException
	{
		File file = new File(filrPath);
		if(!file.exists())
		{
			file.createNewFile();
		}
		
		writeStream = new FileOutputStream(file);
		write = new BufferedWriter(new OutputStreamWriter(writeStream,"UTF-8"));
		write.write(strData+"\t"+"\n");
		
	}
	
	/**
	 * ��ʼ��aveo��ݵ�Schema
	 */
	public void initSchema()
	{
		String schemaFilePath = System.getProperty("user.dir")+File.separator+"conf"+File.separator+"wlw.avsc";
		File file = new File(schemaFilePath); 
		System.out.println(schemaFilePath);
		try
		{
			schema =new Schema.Parser().parse(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʼ��kafka
	 */
	public void initKafka()
	{
		config = new ConsumerConfig(properties);
		consumer = Consumer.createJavaConsumerConnector(config);
	}
	
	/**
	 * ��ʼ��kafka�������ļ�
	 * @throws IOException 
	 */
	public void initProperties() throws IOException
	{
		InputStream is = new FileInputStream(System.getProperty("user.dir")+File.separator+"conf"+File.separator+"kafka-conf.properties");
		properties.load(is);
	}
}
