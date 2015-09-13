package com.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;

/**
 * Created by titanic on 15-9-13.
 */
public class ReadHdfsAvro
{

    private String hdfsUrl = "hdfs://titanic:8020";

   public static void main(String[] args)
   {

   }

    /**
     * 初始化FileSystem
     * @return
     */
    public FileSystem initFs()
    {
        Configuration conf = new Configuration();

        //多线程中，关闭当前线程的FileSystem，其他线程的FileSystem不会关闭
        conf.set("fs.hdfs.impl.disable.cache","true");
        conf.set("fs.defaultFS",hdfsUrl);

        FileSystem fs = null;

        try
        {
            fs = FileSystem.get(conf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return fs;
    }
}
