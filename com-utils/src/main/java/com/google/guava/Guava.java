package com.google.guava;

import com.google.common.base.Charsets;
import com.google.common.collect.*;
import com.google.common.io.Files;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Google Guava使用教程
 *
 *  1. 使用和避免 null：null 有语言歧义， 会产生令人费解的错误， 反正他总是让人不爽。很多 Guava 的工具类在遇到 null 时会直接拒绝或出错，而不是默默地接受他们。
 　　2. 前提条件：更容易的对你的方法进行前提条件的测试。
 　　3. 常见的对象方法： 简化了Object常用方法的实现， 如 hashCode() 和 toString()。
 　　4. 排序： Guava 强大的 "fluent Comparator"比较器， 提供多关键字排序。
 　　5. Throwable类： 简化了异常检查和错误传播。
 */
public class Guava
{
    public static void main(String[] args)
    {
//        fileRead();
        intTest();
    }

    public static void collectionTets()
    {
        // 普通Collection的创建
        List<String> list = Lists.newArrayList();
        Set<String> set = Sets.newHashSet();
        Map<String, String> mam = Maps.newHashMap();

        // 不变Collection的创建,可以直接初始化不可变值
        ImmutableList<String> iList = ImmutableList.of("a","b","c","d");
        ImmutableMap<String,String> iMap = ImmutableMap.of();
        ImmutableSet<String> iSet = ImmutableSet.of();

    }

    //读取文件
    public static void fileRead()
    {
        File file = new File("/home/titanic/soft/School.json");
        List<String> list = null;
        try
        {
            list = Files.readLines(file, Charsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (list != null && list.size() >0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                System.out.println(list.get(i));
            }
        }
    }

    /**
     * 基本操作
     */
    public static void intTest()
    {
        int[] arr = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        //比较大小
        int intCmp = Ints.compare(2, 1);
        System.out.println(intCmp);

        int DoubleCmp = Doubles.compare(1.1, 2.2);
        System.out.println(DoubleCmp);

        int index = Ints.indexOf(arr, 2);
        System.out.println(2);

        //数组是否包含包含1
        boolean contains = Ints.contains(arr, 1);

        int max = Ints.max(arr);
        System.out.println("max："+max);
        System.out.println(Ints.min(arr));

        //合并数组
        int[] arr3 = Ints.concat(arr,arr2);
        for (int i = 0; i < arr3.length; i++)
        {
            System.out.println(arr3[i]);
        }

    }
}
