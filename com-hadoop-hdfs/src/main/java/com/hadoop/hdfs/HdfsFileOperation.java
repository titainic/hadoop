package com.hadoop.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * HDFS文件操作
 */
public class HdfsFileOperation
{
    private String hdfsUrl = "hdfs://titanic:8020";


    public static void main(String[] args)
    {
        HdfsFileOperation hfo = new HdfsFileOperation();

        FileSystem fs = hfo.initFs();

        hfo.createHdfsDir(fs);

    }

    /**
     * 创建文件夹
     * @param fs
     * @return
     */
    public boolean createHdfsDir(FileSystem fs)
    {
        Path path = new Path("/titanic");
        boolean flag = false;
        try
        {
            flag = fs.mkdirs(path);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 创建文件，并写入字符串
     * @param fs
     */
    public void createHdfsFile(FileSystem fs)
    {
        Path path = new Path("/titanic/titanic.txt");
        FSDataOutputStream out = null;

        try
        {
            out = fs.create(path);
            out.writeChars("titanic");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

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
