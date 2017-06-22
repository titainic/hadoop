package com.guava.example;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import java.util.Map;

/**
 * guava table对象
 */
public class TableExample
{
    public static void main(String[] args)
    {
        //列，行，结果集
        Table<String, String,Map<String,String>> table = HashBasedTable.create();
        Map<String, String> map = Maps.newHashMap();
        map.put("x", "y");
        table.put("1", "1", map);

        Map<String, String> map1 = Maps.newHashMap();
        map1.put("x1", "y1");
        table.put("2", "2", map1);

        Map<String, String> map2 = Maps.newHashMap();
        map2.put("x2", "y2");
        table.put("3", "5", map2);

        Map<String, String> map3 = Maps.newHashMap();
        map3.put("x4", "y4");
        table.put("4", "7", map3);

        //根据列和行获取数据
        System.out.println(table.get("3","5"));

        //所有结果集
        System.out.println(table.values());

        //第一个行
        System.out.println(table.row("4"));

        //第二个列
        System.out.println(table.column("5"));

    }
}
