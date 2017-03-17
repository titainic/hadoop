package com.power.demo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * java7实现长单词出现的次数
 * spark　wordcount
 */
public class Java7CollectionTest
{
    public static void main(String[] args)
    {
        try
        {
            String contexts = new String(Files.readAllBytes(Paths.get("/home/titanic/soft/intellij_work/hadoop/com-zjava-demo/pom.xml")), java.nio.charset.StandardCharsets.UTF_8);
            List<String> words = Arrays.asList(contexts.split("\n"));

            int count = 0;
            for (String w : words)
            {
                if (w.length() > 12)
                {
                    count++;
                }
            }
            System.out.println(count);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
