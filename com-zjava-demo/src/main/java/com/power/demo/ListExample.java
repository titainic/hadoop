package com.power.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by titanic on 17-3-13.
 */
public class ListExample
{
    public static void main(String[] args)
    {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < 100; i++)
        {
            list.add(i+"");
        }

        List<String> list2 = new ArrayList<String>();

        for (int i = 20; i < 30 ; i++)
        {
            list2.add(i+"");
        }


        System.out.println(Arrays.asList(list).contains(Arrays.asList(list2)));
        System.out.println(list.contains(list2));
    }
}
