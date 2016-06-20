package com.titanic.hive;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.thrift.TException;

import java.util.List;

/**
 *  HiveCilent测试代码，测试OK
 */
public class HiveMateData
{
    public static void main(String[] args) throws TException
    {
        HiveConf hiveConf = new HiveConf();

        //在resources目录下，hive版本是0.13.0
        hiveConf.addResource(new Path("file:///home/titanic/soft/intellij_work/hadoop/com-hadoop-hive/src/main/resources/hive-site.xml"));

        HiveMetaStoreClient metastoreClient = new HiveMetaStoreClient(hiveConf);
        List<FieldSchema> list = metastoreClient.getFields("default", "test_bin");

        for (int i = 0; i < list.size(); i++)
        {
            System.out.println("Column names : " + list.get(i).getName() + ", type:" + list.get(i).getType());
        }
        metastoreClient.close();
    }
}
