package com.titanic.hive;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.ql.Driver;
import java.util.Map;

/**
 * 1.client 获取用户查询，解析用户输入的命令，提交给Driver；
 * 2.Driver 结合编译器（COMPILER）和元数据库（METASTORE），对用户查询进行编译解析；
 * 3.根据解析结果（查询计划）生成MR任务提交给Hadoop执行；
 * 4.获取最终结果；
 */
public class HiveClient
{
    private HiveConf hiveConf = null;
    private Driver driver = null;
    private HiveMetaStoreClient metaStoreClient= null;

    public HiveClient()
    {
        hiveConf = new HiveConf(HiveClient.class);
    }

    public HiveClient(Map<String,String> map)
    {
        this();
        appendConfiguration(map);
    }

    /**
     * 获取hive配置属性
     */
    private void appendConfiguration(Map<String,String> map)
    {
        if(map != null && map.size() >0)
        {
            for (Map.Entry<String, String> e : map.entrySet())
            {
//                hiveConf.set
            }
        }
    }
}
