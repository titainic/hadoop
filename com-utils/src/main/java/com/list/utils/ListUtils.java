package com.list.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by titanic on 17-5-25.
 */
public class ListUtils
{
    /**
     * list,sum总数,pageSize每个list大小
     */
    public static void subList()
    {
        int sum = 125654, pageSize = 10;
        List list = new ArrayList();
        for (int i = 1; i < sum; i++)
        {
            list.add(i);
        }

        int totalSize = list.size();
        int totalPage = (totalSize - 1) / pageSize + 1;//页码，逢1进1
        if (totalSize < pageSize)
        {
            pageSize = list.size();
        }

        for (int i = 0; i < totalPage; i++)
        {
            int pageNo = i + 1;
            List sublist = list.subList((pageNo - 1) * pageSize, pageNo * pageSize > totalSize ? (totalSize) : pageNo * pageSize);
            System.out.println("第" + pageNo + "次保存" + pageSize + "条数据到数据库....");
            System.out.println(sublist);//每次循环保存的数据输出
            System.out.println(list.size());
        }
    }
}
