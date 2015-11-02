package com.spark.avro;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.file.*;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * avro操作
 */
public class AvroOperation {


    private Schema sourceSchema;
    private Schema targetSchema;
    private Schema.Parser parser=null;

    /**
     * 初始化schema
     */
    public void initSchema(){
        parser = new Schema.Parser();
        String sourcePath=System.getProperty("user.dir")+File.separator+"conf/avro"+File.separator+"ap.avsc";
        String targetPath=System.getProperty("user.dir")+File.separator+"conf/avro_new"+File.separator+"ap.avsc";
        File sourceFile=new File(sourcePath);
        File targetFile=new File(targetPath);
        try{
            sourceSchema = parser.parse(sourceFile);
            targetSchema = parser.parse(targetFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * avro写入数据
     */
    public void testWriteAvro(){
        try {
            File f = new File("/home/eddianliu/user.avro");
            long startTime  = System.currentTimeMillis();

            DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(sourceSchema);
            DataFileWriter<GenericRecord> df = new DataFileWriter<GenericRecord>(writer);
            if(!f.exists()){
                df.create(sourceSchema, f);
            }else{
                df.appendTo(f);
            }
            List<GenericRecord> records = new ArrayList<GenericRecord>();

            int messageNo = 0;
            while (messageNo<20000) {
                GenericRecord datum = new GenericData.Record(sourceSchema);

                datum.put("name", "andy" + messageNo);
                datum.put("favorite_number", 11 + messageNo);
                datum.put("favorite_color", "red" + messageNo);
                String messageStr = "Message_" + messageNo;
                messageNo++;
                records.add(datum);
            }

            if(records.size()>0){
                for (GenericRecord record : records) {
                    df.append(record);
                }
            }

            df.flush();

            df.close();
            //fos.close();
            f.renameTo(new File("/home/eddianliu/user.avro"));
            System.out.println(f.getName() +" 写入文件耗时：" + (System.currentTimeMillis()-startTime) +"ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
//    public void testData(){
//        SysConfig.getInstance().init();
//        HDFSDataCache.init(SysConfig.getInstance().getJdbcDriver(), SysConfig.getInstance().getJdbcConStr(),
//                SysConfig.getInstance().getUname(),SysConfig.getInstance().getPwd(),SysConfig.getInstance().getQryStr());
//        Map<String,String> map = HDFSDataCache.macCache;
//        Set<String> set = map.keySet();
//        for (String string : set) {
//            System.out.println(string + ":" + map.get(string));
//        }
//
//    }

    /**
     * 操作本地的avro数据
     */
    public void testOperatAvro(){
        File target = new File("/home/eddianliu/user_new.avro");
        File source = new File("/home/eddianliu/user.avro");
        DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(sourceSchema);
        DataFileReader<GenericRecord> df = null;
        DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(targetSchema);
        DataFileWriter<GenericRecord> df1 = new DataFileWriter<GenericRecord>(writer);
        try{
            df = new DataFileReader<GenericRecord>(source, reader);
            df1.create(targetSchema, target);

            while(df.hasNext()){
                GenericRecord record = df.next();

                GenericRecord datum = new GenericData.Record(targetSchema);
                datum.put("name", record.get("name"));
                datum.put("favorite_number", record.get("favorite_number"));
                datum.put("favorite_color", record.get("favorite_color"));
                datum.put("desc", record.get("name") + "desc");
                df1.append(datum);
            }
            df1.flush();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                df.close();
                df1.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取hdfs里面的avro文件
     */
    public void testRemoteAvro(){

        Configuration conf = new Configuration();
        try {
            String uri = "hdfs://t1s3:8020/usr/data_wx/20150722/LBS_AP/7470a857-5cf9-4b69-a01c-eae019c7bf51.avro";
            FileSystem fs = FileSystem.get(URI.create(uri),conf);
            Path path = new Path(uri);
            InputStream is = fs.open(path);
            DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(sourceSchema);

            DataFileStream<GenericRecord> dfs = new DataFileStream<GenericRecord>(is, reader);

            List<Field> fields = dfs.getSchema().getFields();
            int count = 0;
            while(dfs.hasNext()){
                GenericRecord record = dfs.next();
                for (Field field : fields) {
                    System.out.println(field.name() + "======" + record.get(field.name()));

                }
                count++;
                System.out.println("记录总数为: " + count);
            }
            dfs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    /**
//     * 判断avro数据在hdfs上面存在吗?
//     */
//    public void testHDFS(){
//        try {
//            boolean flag = HDFSUtils.isExsitsHDFSFile("hdfs://t1s3:8020/usr/data_wx/20150722/LBS_AP/7470a857-5cf9-4b69-a01c-eae019c7bf51.avro");
//            System.out.println(flag);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    /**
     * avro读取数据
     */
    public void testReadAvro(){
        File file = new File("/home/eddianliu/avro/20150722/LBS_AP/1445496870078.avro");
        DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(targetSchema);
        DataFileReader<GenericRecord>  df = null;
        try {
            df = new DataFileReader<GenericRecord>(file, reader);
            int count=0;
            while(df.hasNext()){
                GenericRecord record = df.next();
                System.out.println(record);

                count++;
            }
            System.out.println("记录总数为: " + count);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                df.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 要测试,读取hdfs里面的avro文件
     */
    public void readHdfsAvroForSeekableInput()
    {

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "/home/eddianliu/avro/20150722/LBS_AP/1445496870078.avro");
        conf.set("fs.hdfs.impl.disable.cache", "true");
        FileSystem fs = null;

        DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(sourceSchema);
        DataFileReader dataFileReader = null;
        SeekableInput input = null;
        FileReader<GenericRecord> fr = null;

        try
        {
            fs = FileSystem.get(conf);

            //要测试,第一种使用过fs,conf没有使用过
//          input = new FsInput(new Path("path"), fs);
            input = new FsInput(new Path("path"), conf);
            dataFileReader = new DataFileReader(input, reader);
            fr = dataFileReader.openReader(input, reader);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        long count = 0;
        while (fr.hasNext())
        {
            count += 1;
            GenericRecord tableData = fr.next();

            tableData.toString();
        }





    }

}