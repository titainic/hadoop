package com.google.guava;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Google Guava使用教程
 * <p/>
 * 1. 使用和避免 null：null 有语言歧义， 会产生令人费解的错误， 反正他总是让人不爽。很多 Guava 的工具类在遇到 null 时会直接拒绝或出错，而不是默默地接受他们。
 * 　　2. 前提条件：更容易的对你的方法进行前提条件的测试。
 * 　　3. 常见的对象方法： 简化了Object常用方法的实现， 如 hashCode() 和 toString()。
 * 　　4. 排序： Guava 强大的 "fluent Comparator"比较器， 提供多关键字排序。
 * 　　5. Throwable类： 简化了异常检查和错误传播。
 */
public class Guava
{
    public static void main(String[] args)
    {
        runSet();
//        runMap();
//        joiner_Splitter();
//        bigFile();
//        fileRead();
//        intTest();
//        matches();
    }

    public static void collectionTets()
    {
        // 普通Collection的创建
        List<String> list = Lists.newArrayList();
        Set<String> set = Sets.newHashSet();
        Map<String, String> mam = Maps.newHashMap();

        //list转数组
        List<Integer> listInt = Lists.newArrayList(1, 2, 3);
        int[] intsx = Ints.toArray(listInt);

        // 不变Collection的创建,可以直接初始化不可变值
        ImmutableList<String> iList = ImmutableList.of("a", "b", "c", "d");
        ImmutableMap<String, String> iMap = ImmutableMap.of();
        ImmutableSet<String> iSet = ImmutableSet.of();

    }

    //读取文件
    public static void fileRead()
    {
        //读文件
        File file = new File("/home/titanic/soft/School.json");
        List<String> list = null;
        try
        {
            list = Files.readLines(file, Charsets.UTF_8);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                System.out.println(list.get(i));
            }
        }
    }

    /**
     * 读取大文件
     * <p/>
     * 注意这里的readLines方法返回的是List<String>的接口，这对于大文件处理是会有问题的。
     * 大文件处理可以使用readLines方法的另一个重载。下面的例子演示从一个大文件中逐行读取文本，并做行号计数。
     */
//    public static void bigFile()
//    {
//        File file = new File("/home/titanic/soft/spark/spark-1.6.1-bin-hadoop2.6/README.md");
//        CounterLine cl = new CounterLine();
//        List<String> list = null;
//        try
//        {
//            list = (List<String>) Files.readLines(file, Charsets.UTF_8, cl);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        for (String s : list)
//        {
//            System.out.println(s);
//        }
//
//    }

    /**
     * 这个readLines的重载，需要我们实现一个LineProcessor的泛型接口，
     * 在这个接口的实现方法processLine方法中我们可以对行文本进行处理，
     * getResult方法可以获得一个最终的处理结果，这里我们只是简单的返回了一个行计数。
     * <p/>
     * 另外还有readBytes方法可以对文件的字节做处理，readFirstLine可以返回第一行的文本，
     * Files.toString(File,Charset)可以返回文件的所有文本内容。
     */
//    static class CounterLine implements LineProcessor
//    {
//
//        private int rowNum = 0;
//
//        public boolean processLine(String s) throws IOException
//        {
//            rowNum++;
//            return false;
//        }
//
//        public Integer getResult()
//        {
//            return rowNum;
//        }
//    }


    /**
     * 基本操作,
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
        System.out.println("max：" + max);
        System.out.println(Ints.min(arr));

        //合并数组
        int[] arr3 = Ints.concat(arr, arr2);
        for (int i = 0; i < arr3.length; i++)
        {
            System.out.println(arr3[i]);
        }

    }

    //便利字符匹配类 CharMatcher
    public static void matches()
    {
        //判断最后的 matches方法中的值是否在全面的范围
        boolean result = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).matches('5');
        System.out.println(result);

        //保留数字
        String s1 = CharMatcher.DIGIT.retainFrom("abc 123 ref");
        System.out.println(s1);

        //删除int，保留string
        String s2 = CharMatcher.DIGIT.removeFrom("abd 123 ddd");
        System.out.println(s2);
    }

    public static void joiner_Splitter()
    {
        // 使用 "/" 串联字符串
        String[] subdirs = {"usr", "local", "lib"};
        String path = Joiner.on("/").join(subdirs);
        System.out.println(path);   //   usr/local/lib

        // 使用 "," 切分字符串并去除空串与空格
        String s = "asd,,,,dasdqw,,,qw,qw,";
        Iterable<String> i = Splitter.on(",").omitEmptyStrings().trimResults().split(s);
        for (String x : i)
        {
            System.out.println(x);
        }
    }

    /**
     * 使用自定义回调方法对Map的每个Value进行操作
     * 类似与spark的map操作
     */
    public static void runMap()
    {
        ImmutableMap<String, Double> map = ImmutableMap.of("a", 1.1, "b", 2.2);
        Map<String, Double> map2 = Maps.transformValues(map, new Function<Double, Double>()
        {
            double d = 2;

            public Double apply(Double aDouble)
            {

                return aDouble * d;
            }
        });
        System.out.println(map2);
    }

    //Set集合的合集，交集，差集
    public static void runSet()
    {
        Set<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5, 6);
        Set<Integer> setB = Sets.newHashSet(5, 6, 7, 8, 9, 10);

        //去掉集合中重复的，病合并不同的
        Sets.SetView<Integer> union = Sets.union(setA, setB);
        System.out.println(union);

        //去掉A，与B中相同的值，保留A中剩下的值
        Sets.SetView<Integer> difference = Sets.difference(setA, setB);
        System.out.println(difference);


        //去掉B中与A中相同 的值,保留B中剩下的值
        Sets.SetView<Integer> differencex = Sets.difference(setB, setA);
        System.out.println(differencex);

        //取集合中相同的值
        Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
        System.out.println(intersection);
    }


    public static void runMapxx()
    {

    }
}
