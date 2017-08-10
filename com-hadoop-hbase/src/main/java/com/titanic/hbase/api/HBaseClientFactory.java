/*******************************************************************************
 *
 * Pentaho Big Data
 *
 * Copyright (C) 2002-2016 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package com.titanic.hbase.api;

import com.titanic.hbase.kerberos.KerberosConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HBaseClientFactory
{

    private Logger logger = Logger.getLogger(HBaseClientFactory.class);

    private Connection conn = null;
    private final Configuration conf;

    public HBaseClientFactory(Configuration conf) throws Exception
    {
        logger.info("kerberos开始验证...");
        KerberosConfiguration kc = new KerberosConfiguration(conf);
        this.conf = kc.login();
        System.out.println(conf.get("sun.security.krb5.debug"));
        if (conf != null)
        {
            conn = ConnectionFactory.createConnection(kc.login());
            logger.info("kerberos验证结束...");
        } else
        {
            conn = null;
        }
    }

    public synchronized Connection getConnection() throws IOException
    {
        if (conn == null)
        {
            conn = ConnectionFactory.createConnection(conf);
        }

        return conn;
    }

    /**
     * 获取hbase表
     * @param tableName
     * @return
     */
    public HBaseTable getHBaseTable(final String tableName)
    {
        try
        {
            return new HBaseTable(getConnection(), tableName);
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取HBaseAdmin
     * @return
     */
    public HBaseAdmin getHBaseAdmin()
    {
        try
        {
            return new HBaseAdmin(getConnection());
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭hbase资源
     */
    public void close()
    {
        try
        {
            conn.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 描述一个Transwarp HBase表的所有细节,包括列族等信息。
     * @param tableName
     * @return
     */
    public HTableDescriptor getHBaseTableDescriptor(String tableName)
    {
        return new HTableDescriptor(TableName.valueOf(tableName));
    }

    /**
     * 表封装
     * @param tableObject
     * @return
     */
    public HBaseTable wrap(Object tableObject)
    {
        if (tableObject == null)
        {
            throw new NullPointerException("null as a table was passed");
        }

        Table tab = null;
        if (tableObject instanceof Table)
        {
            tab = (Table) tableObject;
            return new HBaseTable(tab);
        }

        throw new IllegalArgumentException("Type mismatch:" + Table.class.getCanonicalName() + " was expected");
    }


    public HBasePut getHBasePut(byte[] key)
    {
        return new HBasePut(key);
    }

}