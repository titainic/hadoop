package com.power.demo;

import java.util.LinkedList;
import java.util.List;

/**
 * jdk8 lambda使用被动迭代
 */
public class JDK8Lambda
{

    public static void main(String[] args)
    {
        List<String> names = new LinkedList<String>();
        names.add("Apple");
        names.add("Orange");

//        names.forEach(name-> System.out.println(name));
    }
}
