package com.power.demo;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Comparable排序
 */
public class ComparableStudent implements Comparable<ComparableStudent>
{


    public String name;
    public int score;

    public ComparableStudent(String name, int score)
    {
        this.name = name;
        this.score = score;
    }

    public int compareTo(ComparableStudent o)
    {
        if (o.score < this.score)
        {
            return 1;
        } else if (o.score > this.score)
        {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("name:");
        sb.append(name);
        sb.append("  ");
        sb.append("score:");
        sb.append(score);

        return sb.toString();
    }

    public static void main(String[] args)
    {
        TreeMap map = new TreeMap();
        ComparableStudent s1 = new ComparableStudent("1", 100);
        ComparableStudent s2 = new ComparableStudent("2", 99);
        ComparableStudent s3 = new ComparableStudent("3", 97);
        ComparableStudent s4 = new ComparableStudent("4", 91);

        map.put(s1, new studentDetailInfo(s1));
        map.put(s2, new studentDetailInfo(s2));
        map.put(s3, new studentDetailInfo(s3));
        map.put(s4, new studentDetailInfo(s4));

        //打印分数位于s4和s2之间的人
        Map map1 = map.subMap(s4, s2);
        for(Iterator it = map1.keySet().iterator();it.hasNext();)
        {
            ComparableStudent key = (ComparableStudent) it.next();
            System.out.println(key+"->"+map.get(key));
        }
        System.out.println("subMap end　-1");

        //打印分数比s1低的人
        map1 = map.headMap(s1);
        for (Iterator it = map1.keySet().iterator();it.hasNext();)
        {
            ComparableStudent key =  (ComparableStudent) it.next();
            System.out.println(key+"->"+map.get(key));
        }
        System.out.println("subMap end -2");

        //打印分数比s1高的人
        map1 = map.tailMap(s1);
        for (Iterator it = map1.keySet().iterator();it.hasNext();)
        {
            ComparableStudent key =  (ComparableStudent) it.next();
            System.out.println(key+"->"+map.get(key));
        }
        System.out.println("subMap end -3");



    }


}

class studentDetailInfo
{
    ComparableStudent s ;

    public studentDetailInfo(ComparableStudent s)
    {
        this.s = s;
    }

    @Override
    public String toString()
    {

        return s.name + "'s detail information'";
    }
}
