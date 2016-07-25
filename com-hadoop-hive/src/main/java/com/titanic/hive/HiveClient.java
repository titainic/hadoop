package com.titanic.hive;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.cli.CliSessionState;
import org.apache.hadoop.hive.common.StatsSetupConst;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.MetaStoreUtils;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.CommandNeedRetryException;
import org.apache.hadoop.hive.ql.Driver;
import org.apache.hadoop.hive.ql.processors.CommandProcessorResponse;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 用于执行Hive cmd命令的客户端，不适合查询数据库。测试通过
 */
public class HiveClient
{
    private HiveConf hiveConf = null;
    private Driver driver = null;
    private HiveMetaStoreClient metaStoreClient = null;

    public HiveClient()
    {
        hiveConf = new HiveConf(HiveClient.class);

    }

    /**
     * hive-site.xml配置文件的路径
     * @param hiveConfigPath
     */
    public HiveClient(String hiveConfigPath)
    {
        hiveConf = new HiveConf(HiveClient.class);
        hiveConf.addResource(new Path("file://"+hiveConfigPath));
    }

    public HiveClient(Map<String, String> confMap)
    {
        this();
        appendConfiguration(confMap);
    }

    public HiveConf getHiveConf()
    {
        return hiveConf;
    }

    /**
     * 获取driver
     *
     * @return
     */
    private Driver getDriver()
    {
        if (driver == null)
        {
            driver = new Driver(hiveConf);
            SessionState.start(new CliSessionState(hiveConf));
        }
        return driver;
    }

    /**
     * 追加配置
     *
     * @param confMap
     */
    private void appendConfiguration(Map<String, String> confMap)
    {
        if (confMap != null && confMap.size() > 0)
        {
            for (Map.Entry<String, String> e : confMap.entrySet())
            {
                hiveConf.set(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * 执行hive sql命令
     *
     * @param hql
     * @throws CommandNeedRetryException
     * @throws IOException
     */
    public void executeCmdHQL(String hql) throws CommandNeedRetryException, IOException
    {
        CommandProcessorResponse response = getDriver().run(hql);
        int responseCode = response.getResponseCode();
        if (responseCode != 0)
        {
            throw new IOException("Failed to execute hql [" + hql + "], error message is: " + response.getErrorMessage());
        }

    }

    public void executeCmdHQL(String[] hqls) throws IOException, CommandNeedRetryException
    {
        for (String sql : hqls)
        {
            executeCmdHQL(sql);
        }
    }

    private HiveMetaStoreClient getMetaStoreClient() throws MetaException
    {
        if (metaStoreClient == null)
        {
            metaStoreClient = new HiveMetaStoreClient(hiveConf);
        }
        return metaStoreClient;
    }

    /**
     * 获取hive的表
     *
     * @param database
     * @param tableName
     * @return
     * @throws TException
     */
    public Table getHiveTable(String database, String tableName) throws TException
    {
        return getMetaStoreClient().getTable(database, tableName);
    }

    /**
     * 获取hive的table中fields
     *
     * @param database
     * @param tablesName
     * @return
     * @throws TException
     */
    public List<FieldSchema> getHiveTableFields(String database, String tablesName) throws TException
    {
        return getMetaStoreClient().getFields(database, tablesName);
    }

    /**
     * 获取hive的table在HDFS中存储的路径
     *
     * @param database
     * @param tableName
     * @return
     * @throws TException
     */
    public String getHiveTableLocation(String database, String tableName) throws TException
    {
        Table t = getHiveTable(database, tableName);
        return t.getSd().getLocation();
    }

    public long getFieldSizeForTable(Table table)
    {
        return getBasicStatForTable(new org.apache.hadoop.hive.ql.metadata.Table(table), StatsSetupConst.TOTAL_SIZE);
    }

    public long getFieldNumberForTable(Table table)
    {
        return getBasicStatForTable(new org.apache.hadoop.hive.ql.metadata.Table(table), StatsSetupConst.NUM_FILES);
    }

    /**
     * 获取hive中所有数据库名
     *
     * @return
     * @throws MetaException
     */
    public List<String> getHiveDbNames() throws MetaException
    {
        return getMetaStoreClient().getAllDatabases();
    }

    /**
     * 获取hive某一个数据库中所有表的名字
     *
     * @param database
     * @return
     * @throws MetaException
     */
    public List<String> getHiveTableNames(String database) throws MetaException
    {
        return getMetaStoreClient().getAllTables(database);
    }

    public static long getBasicStatForTable(org.apache.hadoop.hive.ql.metadata.Table table, String statType)
    {
        Map<String, String> params = table.getParameters();
        long result = 0;
        if (params != null)
        {
            try
            {
                result = Long.parseLong(params.get(statType));
            } catch (NumberFormatException e)
            {
                result = 0;
            }
        }
        return result;
    }

    /**
     * 非本地的表？？？
     *
     * @param database
     * @param tableName
     * @return
     * @throws TException
     */
    public boolean isNativeTable(String database, String tableName) throws TException
    {
        return !MetaStoreUtils.isNonNativeTable(getMetaStoreClient().getTable(database, tableName));
    }

    public void clearConfig()
    {
        hiveConf.clear();
    }

    /**
     * 是否是外部表
     * @param table
     * @return
     */
    public static boolean isExternalTable(Table table)
    {
        if (table == null)
        {
            return false;
        }
        Map<String, String> params = table.getParameters();
        if (params == null)
        {
            return false;
        }

        return "TRUE".equalsIgnoreCase(params.get("EXTERNAL"));
    }

    /**
     * 有时间研究下hive的这个工具类
     * API ： https://hive.apache.org/javadocs/r0.13.1/api/metastore/org/apache/hadoop/hive/metastore/MetaStoreUtils.html
     */
    public void useMetaStoreUtils()
    {

    }
}
