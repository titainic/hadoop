/*******************************************************************************
 *
 * Pentaho Big Data
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
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

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

public class HBaseAdmin
{
    private final Admin admin;

    public HBaseAdmin(Connection conn) throws IOException
    {
        admin = conn.getAdmin();
    }

    /**
     * 表是否存在
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean tableExists(String tableName) throws IOException
    {
        return admin.tableExists(TableName.valueOf(tableName));
    }

    /**
     * 列出所有表
     * @return
     * @throws IOException
     */
    public HTableDescriptor[] listTables() throws IOException
    {
        return admin.listTables();
    }

    /**
     * 表是否失效
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean isTableDisabled(String tableName) throws IOException
    {
        return admin.isTableDisabled(TableName.valueOf(tableName));
    }

    /**
     * 表是否生效
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean isTableEnabled(String tableName) throws IOException
    {
        return admin.isTableEnabled(TableName.valueOf(tableName));
    }

    /**
     * 表是否可用
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean isTableAvailable(String tableName) throws IOException
    {
        return admin.isTableAvailable(TableName.valueOf(tableName));
    }

    /**
     * 获取表结构
     * @param tableName
     * @return
     * @throws IOException
     */
    public HTableDescriptor getTableDescriptor(byte[] tableName) throws IOException
    {
        return admin.getTableDescriptor(TableName.valueOf(tableName));
    }

    /**
     * 表生效
     * @param tableName
     * @throws IOException
     */
    public void enableTable(String tableName) throws IOException
    {
        admin.enableTable(TableName.valueOf(tableName));
    }

    /**
     * 表失效
     * @param tableName
     * @throws IOException
     */
    public void disableTable(String tableName) throws IOException
    {
        admin.disableTable(TableName.valueOf(tableName));
    }

    /**
     * 删除表
     * @param tableName
     * @throws IOException
     */
    public void deleteTable(String tableName) throws IOException
    {
        admin.deleteTable(TableName.valueOf(tableName));
    }

    /**
     * 创建表
     * @param tableDesc
     * @throws IOException
     */
    public void createTable(HTableDescriptor tableDesc) throws IOException
    {
        admin.createTable(tableDesc);
    }

    /**
     * 关闭实例的所用资源
     * @throws IOException
     */
    public void close() throws IOException
    {
        admin.close();
    }
}