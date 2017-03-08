package com.power.demo;

/**
 *静态变量和实例变量对比
 */
public class VariableCompare
{
    public static int b = 0;

    public static void main(String[] args)
    {
        int a = 0;
        long starttime = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++)
        {
            a++;
        }

        System.out.println(System.currentTimeMillis()-starttime);

        starttime = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++)
        {
            b++;
        }
        System.out.println(System.currentTimeMillis()-starttime);
    }

}
