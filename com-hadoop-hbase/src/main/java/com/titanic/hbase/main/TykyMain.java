package com.titanic.hbase.main;

import com.titanic.hbase.mysql.MySqlJdbc;
import org.apache.hadoop.hbase.client.Put;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by titanic on 17-5-25.
 */
public class TykyMain
{
    private static Logger logger = Logger.getLogger(TykyMain.class);
    public static void main(String[] args)
    {
        MySqlJdbc mySql = new MySqlJdbc();
        List<Map<String,String>> msqlList = mySql.queryForList();
        HBaseService hBaseService = new HBaseService();
        hBaseService.ctrateHbaseTable();
        logger.info("mysql查询"+msqlList.size()+"条数据");
        int pageSize = 1000;
        logger.info("批量插入hbase数据为"+pageSize+"条,提交一次到hbase");


        int totalSize = msqlList.size();
        int totalPage = (totalSize - 1) / pageSize + 1;//页码，逢1进1
        if (totalSize < pageSize)
        {
            pageSize = msqlList.size();
        }
        for (int i = 0; i < totalPage; i++)
        {
            int pageNo = i + 1;
            int start = (pageNo - 1) * pageSize;
            int end = pageNo * pageSize > totalSize ? (totalSize) : pageNo * pageSize;

            if(end == 12000)
            {
                System.out.println(end);
            }

            List sublist = msqlList.subList(start,end);
            logger.info("第" + pageNo + "次保存" + pageSize + "条数据开始插入到hbase数据库....");
            List<Put> putList = hBaseService.mysqlTransformHBase(sublist);
            hBaseService.insertHBaseData(putList);
            logger.info("第" + pageNo + "次保存" + pageSize + "条数据完成插入到hbase数据库******");
            putList.clear();
        }
        logger.info("关闭hbse链接资源");
        hBaseService.close();
    }
}

