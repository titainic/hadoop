package com.hadoop.dnn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.server.namenode.NameNode;

import java.io.IOException;

/**
 * 检查NameNode状态.
 */
public class CheckNameStaus
{
    static Configuration conf = null;

    static
    {
        conf = new Configuration();
        conf.addResource(new Path("file:///home/titanic/soft/hadoop/hadoop-2.6.4/etc/hadoop/core-site.xml"));
        conf.addResource(new Path("file:///home/titanic/soft/hadoop/hadoop-2.6.4/etc/hadoop/hdfs-site.xml"));
    }

    public static void main(String[] args)
    {
//        getNamNodeInfo();
    }

    public static void getClusterInfo()
    {

    }

    /**
     * 过去NameNode信息
     */
//    public static void getNamNodeInfo()
//    {
//        String[] args = new String[]{};
//        try
//        {
//            NameNode nn = NameNode.createNameNode(args, conf);
//            System.out.println(nn.getHttpAddress());
//            System.out.println(nn.getState());
//            System.out.println(nn.getClientNamenodeAddress());
//            System.out.println(nn.getFSImage());
//            System.out.println(nn.getHostAndPort());
//            System.out.println(nn.getNamesystem());
//            System.out.println(nn.isActiveState());
//            System.out.println(nn.isInSafeMode());
//            System.out.println(nn.isSecurityEnabled());
//            System.out.println(nn.getTokenServiceName());
//
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//
//    }

    //获取datanode信息
    public static void getDataNodeInfo()
    {
        try
        {
            FileSystem fs = FileSystem.get(conf);
            DistributedFileSystem dfs = (DistributedFileSystem) fs;


            //获取DataNode
            DatanodeInfo[] df = dfs.getDataNodeStats();

            if (df.length >= 1)
            {
                //DataNode信息
                for (DatanodeInfo d : df)
                {
                    System.out.println(d.getName());
                    System.out.println(d.getHostName());
                    System.out.println(d.getSoftwareVersion());
                    System.out.println(d.getAdminState());
                    System.out.println(d.getDependentHostNames());
                    System.out.println(d.getInfoAddr());
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
