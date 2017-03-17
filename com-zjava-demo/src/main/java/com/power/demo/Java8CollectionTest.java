package com.power.demo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by titanic on 17-3-15.
 */
public class Java8CollectionTest
{
    public static void main(String[] args)
    {
        try
        {
            String contexts = new String(Files.readAllBytes(Paths.get("/home/titanic/soft/intellij_work/hadoop/com-zjava-demo/pom.xml")),java.nio.charset.StandardCharsets.UTF_8);
            List<String> words = Arrays.asList(contexts.split("\n"));

            long count = words.parallelStream().filter(new Predicate<String>()
            {
                public boolean test(String s)
                {
                    return s.length() > 12;
                }
            }).count();
            System.out.println(count);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
