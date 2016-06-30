package com.titanic.hive2es;

import com.google.common.collect.Maps;
import org.apache.hadoop.hive.ql.CommandNeedRetryException;

import java.io.IOException;
import java.util.Map;

/**
 *hive同步数据到es
 */
public class HiveToEs
{

    private static String esIndex = "test_bin_es";
    private static String esType = "test_bin_es_type";
    private static String hiveToEsTable = "test_bin_es";

    public static void main(String[] args)
    {
        Map<String, String> map = Maps.newHashMap();
        map.put("id", "int");
        map.put("name", "string");
        map.put("age", "int");
        map.put("tel", "string");

        EsIndexBuilder esIndexBuilder = new EsIndexBuilder();

        try
        {
            esIndexBuilder.buliderEsIndex(map, esIndex, esType);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        createHiveForEs(map,hiveToEsTable,esIndex+"/"+esType);

        InsertHiveToEs();
    }

    public static void InsertHiveToEs()
    {
        HiveClient hiveClient = new HiveClient("/home/titanic/soft/hadoop/hive-2.0.0/conf/hive-site.xml");
        String hql = "INSERT OVERWRITE TABLE test_bin_es  SELECT id, name,age,tel FROM test_bin ";
        try
        {
            hiveClient.executeHQL(hql);
        } catch (CommandNeedRetryException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void createHiveForEs(Map<String, String> map,String hiveToEsTable,String mappingEsIndexTYpe)
    {

        String fields = createHive2EsTableHql(map);

        String hql = "CREATE EXTERNAL TABLE "+hiveToEsTable+"  ( " + fields + " )     \n" +
                "STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'   \n" +
                "TBLPROPERTIES('es.resource' = '"+mappingEsIndexTYpe+"','es.nodes'='192.9.7.4')";

        HiveClient hiveClient = new HiveClient("/home/titanic/soft/hadoop/hive-2.0.0/conf/hive-site.xml");

        try
        {
            hiveClient.executeHQL(hql);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public static String createHive2EsTableHql(Map<String, String> map)
    {
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            sb.append(entry.getKey()).append(" ").append(entry.getValue()).append(",");
        }
        String str = sb.toString();
        str = str.substring(0, str.toString().length() - 1);
        System.out.println(str);
        return str;

    }
}
