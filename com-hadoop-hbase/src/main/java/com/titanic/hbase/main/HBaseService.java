package com.titanic.hbase.main;

import com.titanic.hbase.api.HBaseAdmin;
import com.titanic.hbase.api.HBaseClientFactory;
import com.titanic.hbase.api.HBaseTable;
import com.titanic.hbase.utils.TykyUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

/**
 * hbase插入数据实现
 */
public class HBaseService
{

    private Configuration conf = new Configuration();
    private Connection conn = null;
    private HBaseClientFactory factory = null;
    private String hbaseTableName = null;
    private HBaseTable hBaseTable;

    public HBaseService()
    {
        init();
    }

    /**
     * 初始化hbase集群
     */
    public void init()
    {
        conf.addResource(new Path(TykyUtils.getConfigurationFilePath() + "core-site.xml"));
        conf.addResource(new Path(TykyUtils.getConfigurationFilePath() + "hbase-site.xml"));
        getHbaseConnection();
    }


    /**
     * 获取hbase链接
     *
     * @return
     */
    private Connection getHbaseConnection()
    {
        try
        {
            factory = new HBaseClientFactory(conf);
            conn = factory.getConnection();
            return conn;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入数据
     */
    public void insertHBaseData(List<Put> list)
    {

        ctrateHbaseTable();
        try
        {
            hBaseTable = new HBaseTable(conn,hbaseTableName);
            hBaseTable.setAutoFlush(true);
            hBaseTable.setWriteBufferSize(5 * 1024 * 1024);//5MB
            hBaseTable.putList(list);

            hBaseTable.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            hBaseTable.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 构建表和列族
     *
     */
    public void ctrateHbaseTable()
    {

        Properties pp = TykyUtils.gethbasePropertiesFimaly();
        String HtableName = pp.getProperty("htable");
        String hbaseJSon= pp.getProperty("hFimaly");
        hbaseTableName = HtableName;
        try
        {
            if (!getHbaseAdmin().tableExists(HtableName))
            {
                HTableDescriptor table = new HTableDescriptor(HtableName);
                JSONObject json = new JSONObject(hbaseJSon);
                Set<String> fimalySet = json.keySet();
                for (String fimaly : fimalySet)
                {
                    table.addFamily(new HColumnDescriptor(fimaly));
                }
                getHbaseAdmin().createTable(table);
            }


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public HBaseAdmin getHbaseAdmin()
    {
        return factory.getHBaseAdmin();
    }


    /**
     * 获取属性文件中hbase列族和列属性,构建Put
     * @param mList
     * @return
     */
    public static List<Put> mysqlTransformHBase(List<Map<String, String>> mList)
    {
        List<Put> list = new ArrayList<Put>();
        Properties pp = TykyUtils.gethbasePropertiesFimaly();
        String rowkey = pp.getProperty("hrowkey");

        if (pp.getProperty("hFimaly") != null)
        {
            String fimalyJson = pp.getProperty("hFimaly");
            JSONObject json = new JSONObject(fimalyJson);
            Set<String> fimalySet = json.keySet();
            if (mList != null && mList.size() >0)
            {
                for (Map<String, String> map : mList)
                {
//                    System.out.println(map.get("id"));
                    Put put = new Put(toBytes(map.get(rowkey)));
                    for (String fimaly : fimalySet)
                    {
                        JSONArray cloArray = json.getJSONArray(fimaly);
                        List<Object> cloList = cloArray.toList();
                        for (Object column : cloList)
                        {
//                            System.out.println("fimaly->"+fimaly+",column->"+column+","+column);
                            put.add(toBytes(fimaly), toBytes(String.valueOf(column)), toBytes(map.get(column)+""));
                        }
                    }
                    list.add(put);
                }
            }
        }
        return list;
    }



}
