package com.titanic.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

/**
 * hbase　API操作
 */
public class HBaseClientExample
{
    static HBaseConfiguration hbaseConf = null;
    static Configuration conf = null;
    static HBaseAdmin admin = null;

    static
    {
        conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        hbaseConf = new HBaseConfiguration(conf);
        try
        {
            admin = new HBaseAdmin(conf);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
//        createHbaseTable();
//        listHbaseTable();

//        addHbaseFamily("emp", null);
//        deleteHbaseFamily("emp","family");
//        putHbaseData("emp");
//        updateHbaseData("emp");
//        getHbaseTableData("emp");
//        deleteHbaseTableColumn("emp");
//        scanHbaseTable("emp");
    }


    /**
     * 创建表,和列族
     */
    public static void createHbaseTable()
    {
        try
        {
            //查看test是否存在
            if (admin.tableExists("emp"))
            {
                //删除test
                admin.deleteTable("emp");
            }
            //构建test表
            HTableDescriptor table = new HTableDescriptor("emp");

            //构建test表里面的列族，有２个列族
            table.addFamily(new HColumnDescriptor("personal"));
            table.addFamily(new HColumnDescriptor("professional"));

            admin.createTable(table);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 查看hbase里面的所有表
     */
    public static void listHbaseTable()
    {
        try
        {
            HTableDescriptor[] tables = admin.listTables();
            for (HTableDescriptor table : tables)
            {
                System.out.println(table.getTableName());
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 表失效
     */
    public static void disableHbaseTable(String tableName)
    {
        try
        {
            boolean flg = admin.isTableDisabled(tableName);
            if (flg)
            {
                System.out.println("Table emp is disable");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 表生效
     */
    public static void enableHbaseTable()
    {
        try
        {
            boolean flg = admin.isTableEnabled("emp");
            if (flg)
            {
                System.out.println("Table emp is enable");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 添加列族
     *
     * @param tableName
     * @param family
     */
    public static void addHbaseFamily(String tableName, String family)
    {
        HColumnDescriptor familys = new HColumnDescriptor(family);
        try
        {
            admin.addColumn(tableName, familys);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 删除列族
     *
     * @param tableName
     * @param family
     */
    public static void deleteHbaseFamily(String tableName, String family)
    {
        try
        {
            admin.deleteColumn(tableName, family);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 数据库中是否存在表，当数据库中很多表的时候list不能用的时候，使用这个方法
     *
     * @param tableName
     */
    public static void existsHbaseTable(String tableName)
    {
        try
        {
            admin.tableExists(tableName);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 删除表
     *
     * @param tableName
     */
    public static void deleteHbaseTable(String tableName)
    {
        disableHbaseTable(tableName);
        try
        {
            admin.deleteTable(tableName);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 插入一行数据
     *
     * @param tableName
     */
    public static void putHbaseData(String tableName)
    {
        try
        {
            HTable table = new HTable(conf, tableName);
            Put put = new Put(toBytes("row1"));

            //列族　列名　列名相对于的值
            put.add(toBytes("personal"), toBytes("name"), toBytes("titanic"));
            put.add(toBytes("personal"), toBytes("city"), toBytes("wuhan"));

            put.add(toBytes("professional"), toBytes("designation"), toBytes("manager"));
            put.add(toBytes("professional"), toBytes("salary"), toBytes("50000"));

            table.put(put);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 更新一行表中的数据
     *
     * @param tableName
     */
    public static void updateHbaseData(String tableName)
    {
        try
        {
            HTable table = new HTable(conf, tableName);

            //根据row1更新数据？
            Put put = new Put(toBytes("row1"));
            put.add(toBytes("personal"), toBytes("city"), toBytes("shenz"));
            put.add(toBytes("professional"), toBytes("salary"), toBytes("30986125"));
            table.put(put);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取一行数据
     *
     * @param tableName
     */
    public static void getHbaseTableData(String tableName)
    {
        try
        {
            HTable table = new HTable(conf, tableName);

            Get get = new Get(toBytes("row1"));
            Result result = table.get(get);
            byte[] value = result.getValue(toBytes("personal"), toBytes("city"));

            String city = Bytes.toString(value);
            System.out.println(city);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 删除hbase 一行中列族professional　和personal列族中name的数据
     *
     * @param tableName
     */
    public static void deleteHbaseTableColumn(String tableName)
    {
        try
        {
            HTable table = new HTable(conf, tableName);
            Delete delete = new Delete(toBytes("row1"));

            delete.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("name"));
            delete.addFamily(toBytes("professional"));

            table.delete(delete);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 扫表表
     *
     * @param tableName
     */
    public static void scanHbaseTable(String tableName)
    {
        try
        {
            HTable table = new HTable(conf, tableName);

            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("city"));

            ResultScanner scanner = table.getScanner(scan);

            for (Result result = scanner.next(); result != null; result = scanner.next())
            {
                System.out.println("Found row : " + result);
            }

            scanner.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
