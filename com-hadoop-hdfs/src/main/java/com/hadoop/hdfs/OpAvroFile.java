package com.hadoop.hdfs;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.util.Utf8;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by titanic on 17-6-1.
 */
public class OpAvroFile
{

    /**
     * 构建Schema,即列命信息
     *
     * @param map
     * @param tableName
     * @return
     */
    private Schema makeSchema(Map<String, String> map, String tableName)
    {
        if (map != null && map.size() > 0)
        {
            List<Schema.Field> fields = new ArrayList<Schema.Field>();
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                fields.add(new Schema.Field(entry.getKey(), Schema.create(Schema.Type.STRING), null, null));
            }
            Schema schema = Schema.createRecord(tableName, null, tableName + ".NameSpace", false);
            schema.setFields(fields);
            return (schema);
        }
        return null;
    }

    /**
     * 以avro格式写入数据到hdfs
     * @param listMap 写入的结果集
     * @param conf  hadoop配置
     * @param hdfsFilePath hdfs路径
     * @param fileName 文件名
     * @param tableName 表名
     */
    public void witeHdfsAvro(List<Map<String,String>> listMap, Configuration conf, String hdfsFilePath, String fileName, String tableName)
    {
        if (listMap != null && listMap.size() > 0)
        {
            Schema schema = makeSchema (listMap.get(0),tableName);

            try
            {
                writeHdfsForAvroFile(listMap, schema, hdfsFilePath, fileName, conf);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }



    /**
     *
     * @param schema
     * @param hdfsFilePath
     * @param fileName
     * @param conf
     * @throws IOException
     */
    private void writeHdfsForAvroFile(List<Map<String,String>> listMap, Schema schema, String hdfsFilePath, String fileName, Configuration conf) throws IOException
    {
        FileSystem hdfs = FileSystem.get(conf);

        createSchemaFile(schema,hdfs,hdfsFilePath);

        Path outputFilePath = new Path(hdfsFilePath+"/"+fileName+".avro");
        FSDataOutputStream dataOutputStream = hdfs.create(outputFilePath);

        DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>();
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(writer);
        dataFileWriter.create(schema, dataOutputStream);

        if (listMap != null && listMap.size() > 0)
        {
            for (Map<String, String> map : listMap)
            {
                GenericRecord datum = new GenericData.Record(schema);
                for (Map.Entry<String, String> entry : map.entrySet())
                {
                    datum.put(entry.getKey(), new Utf8(entry.getValue()+""));
                }
                dataFileWriter.append(datum);
            }
        }
        dataFileWriter.close();
        dataOutputStream.close();
    }


    /**
     * 构建Schema文件
     * @param schema
     * @param hdfs
     * @param hdfsFilePath
     * @throws IOException
     */
    private void createSchemaFile(Schema schema, FileSystem hdfs, String hdfsFilePath) throws IOException
    {
        Path path = new Path(hdfsFilePath + "/schema.avsc");
        if (!hdfs.exists(path))
        {
            FSDataOutputStream out = null;
            try
            {
                out = hdfs.create(path);
                out.writeChars(schema.toString());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }

    /**
     * 读取avro文件
     * @param schema
     * @param file
     * @return
     * @throws Exception
     */
    private List<Object> fromJson(Schema schema, File file) throws Exception
    {
        InputStream in = new FileInputStream(file);
        List<Object> data = new ArrayList<Object>();
        try
        {
            DatumReader reader = new GenericDatumReader(schema);
            Decoder decoder = DecoderFactory.get().jsonDecoder(schema, in);
            while (true) data.add(reader.read(null, decoder));
        } catch (EOFException e)
        {
        } finally
        {
            in.close();
        }
        return data;
    }

}
