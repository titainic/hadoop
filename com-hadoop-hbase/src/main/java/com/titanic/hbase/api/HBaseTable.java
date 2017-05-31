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

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.List;

public class HBaseTable
{
    /**
     * 线程安全,替代HTase
     */
    private final Table tab;
    private BufferedMutator mutator = null;
    private boolean autoFlush = true;
    private final Connection conn;

    public HBaseTable(Connection conn, String tableName) throws IOException
    {
        this.conn = conn;
        tab = conn.getTable(TableName.valueOf(tableName));
    }

    /**
     * Constructs read-only HBaseTable
     *
     * @param tab - HBase Table to wrap
     */
    HBaseTable(Table tab)
    {
        this.tab = tab;
        conn = null;
    }

    /**
     * 获取表操作
     * @return
     * @throws IOException
     */
    private synchronized BufferedMutator getBufferedMutator() throws IOException
    {
        if (conn != null)
        {
            /**
             * htable中提供了buffer的功能(BufferedMutator),当buffer达到一定的size或者num后，
             * 会自动后台线程flush到相应的regionserver。 目前这个buffer只针对put(put list)操作并且需要关闭自动提交，
             * 可以异步提交put请求并并flush，其它操作都是同步完成，有些单个的请求是直接同步发送rpc请求，
             * 批量的操作涉及到多个regionserver通信的操作，会分组做成runnable，提交到thread pool并行执行，待所有执行完成后返回。
             */
            if (mutator == null)
            {
                mutator = conn.getBufferedMutator(tab.getName());
            }
        } else
        {
            throw new IOException("Can't mutate the table " + tab.getName());
        }

        return mutator;
    }

    /**
     * 设置hbase客户端缓冲区所占空间大小
     */
    public void setWriteBufferSize(long bufferSize) throws IOException
    {
        tab.setWriteBufferSize(bufferSize);
    }

    /**
     * 设置是否提交
     * @param autoFlush
     * @throws IOException
     */
    public void setAutoFlush(boolean autoFlush) throws IOException
    {
        this.autoFlush = autoFlush;
    }

    /**
     * 是否自动提交.默认true
     * @return
     * @throws IOException
     */
    public boolean isAutoFlush() throws IOException
    {
        return autoFlush;
    }

    /**
     * 获取 scan
     * @param s
     * @return
     * @throws IOException
     */
    public ResultScanner getScanner(Scan s) throws IOException
    {
        return tab.getScanner(s);
    }

    /**
     * 获取get
     * @param toGet
     * @return
     * @throws IOException
     */
    public Result get(Get toGet) throws IOException
    {
        return tab.get(toGet);
    }

    /**
     * 刷新并提交
     * @throws IOException
     */
    public void flushCommits() throws IOException
    {
        getBufferedMutator().flush();
    }

    /**
     * 删除表
     * @param toDel
     * @throws IOException
     */
    public void delete(Delete toDel) throws IOException
    {
        getBufferedMutator().mutate(toDel);
        if (autoFlush)
        {
            getBufferedMutator().flush();
        }
    }

    /**
     * 关闭表资源
     * @throws IOException
     */
    public void close() throws IOException
    {
        tab.close();
        if (mutator != null)
        {
            mutator.close();
        }
    }

    /**
     * 添加数据
     * @param put
     * @throws IOException
     */
    public void put(HBasePut put) throws IOException
    {
        if (put == null)
        {
            throw new NullPointerException("NULL Put passed");
        }
        if (put instanceof HBasePut)
        {
            HBasePut p10 = (HBasePut) put;
            put(p10.getPut());
        } else
        {
            throw new IllegalArgumentException("Unexpected backed HBasePut type passed:" + put.getClass());
        }
    }



    /**
     * 添加数据具体实现
     * @param toPut
     * @throws IOException
     */
    public void put(Put toPut) throws IOException
    {

        getBufferedMutator().mutate(toPut);
        if (autoFlush)
        {
            getBufferedMutator().flush();
        }
    }

    /**
     * 批量添加数据
     * @param toPutList
     * @throws IOException
     */
    public void putList(List<Put> toPutList) throws IOException
    {
        getBufferedMutator().mutate(toPutList);
        if (autoFlush)
        {
            getBufferedMutator().flush();
        }
    }

}