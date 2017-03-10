package com.power.demo;

/**
 * 一维数组和二维数组对比
 */
public class ArrayTest
{

    public static void main(String[] args)
    {
        long start = System.currentTimeMillis();
        int[] arraySingle = new int[1000000];
        int chk = 0;

        for (int i = 0; i < 100 ; i++)
        {
            for (int j = 0; j < arraySingle.length; j++)
            {
                arraySingle[j] = j;
            }
        }
    }
}
