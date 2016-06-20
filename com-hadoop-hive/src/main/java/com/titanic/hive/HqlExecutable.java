package com.titanic.hive;

import org.apache.hadoop.hive.metastore.api.MetaException;

import java.util.List;

/**
 * HiveCilent测试代码，测试OK
 */
public class HqlExecutable
{
    public static void main(String[] args) throws MetaException
    {
        //resources目录下的hive配置文件
        HiveClient hiveClient = new HiveClient("/home/titanic/soft/hive-site.xml");

        List<String> dbNames = hiveClient.getHiveDbNames();

        //获取所有数据，和数据库中表的名称
        for (String db : dbNames)
        {
            System.out.println("db->:" + db);
            List<String> tableNames = hiveClient.getHiveTableNames(db);
            for (String table : tableNames)
            {
                System.out.println("table ->" +table);
            }
            System.out.println("----------------------------");

        }
    }
}
