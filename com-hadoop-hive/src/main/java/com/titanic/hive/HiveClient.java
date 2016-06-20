package com.titanic.hive;

import org.apache.hadoop.conf.Configuration;
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
 * Created by titanic on 16-6-15.
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

    public HiveClient(String hiveConfigPath)
    {
        hiveConf = new HiveConf(HiveClient.class);
        hiveConf.addResource(hiveConfigPath);
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
     * @return
     */
    private Driver getDriver()
    {
        if(driver == null)
        {
            driver = new Driver(hiveConf);
            SessionState.start(new CliSessionState(hiveConf));
        }
        return driver;
    }

    /**
     * 追加配置
     * @param confMap
     */
    private void appendConfiguration(Map<String, String> confMap)
    {
        if(confMap != null && confMap.size() >0)
        {
            for (Map.Entry<String, String> e : confMap.entrySet())
            {
                hiveConf.set(e.getKey(),e.getValue());
            }
        }
    }

    /**
     * 执行hive sql命令
     * @param hql
     * @throws CommandNeedRetryException
     * @throws IOException
     */
    public void executeHQL(String hql) throws CommandNeedRetryException, IOException
    {
        CommandProcessorResponse response = getDriver().run(hql);
        int responseCode = response.getResponseCode();
        if(responseCode != 0)
        {
            throw  new IOException("Failed to execute hql ["+hql+ "], error message is: " + response.getErrorMessage());
        }
    }

    public void exeexecuteHQL(String[] hqls) throws IOException, CommandNeedRetryException
    {
        for(String sql: hqls)
        {
            executeHQL(sql);
        }
    }

    private HiveMetaStoreClient getMetaStoreClient() throws MetaException
    {
        if(metaStoreClient == null)
        {
            metaStoreClient = new HiveMetaStoreClient(hiveConf);
        }
        return metaStoreClient;
    }

    /**
     * 获取hive的表
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
     * @return
     * @throws MetaException
     */
    public List<String> getHiveDbNames() throws MetaException
    {
        return getMetaStoreClient().getAllDatabases();
    }

    /**
     * 获取hive某一个数据库中所有表的名字
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
}
