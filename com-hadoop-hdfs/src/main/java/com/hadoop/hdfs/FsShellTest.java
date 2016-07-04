package com.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.Path;

/**
 * 执行hadoop中hdfs命令
 * 示例代码
 * http://www.programcreek.com/java-api-examples/index.php?api=org.apache.hadoop.fs.FsShell
 */
public class FsShellTest
{
    public static void main(String[] args)
    {
        Configuration conf = new Configuration();
        conf.addResource(new Path("file:///home/titanic/soft/hadoop/hadoop-2.6.4/etc/hadoop/hdfs-site.xml"));
        conf.addResource(new Path("file:///home/titanic/soft/hadoop/hadoop-2.6.4/etc/hadoop/core-site.xml"));
        FsShell shell = new FsShell(conf);

        //命令前面加 -
        String[] cmd = new String[]{"-chmod","u-x","/tmp"};
        try
        {
            int code = shell.run(cmd);
            System.out.println(code);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
