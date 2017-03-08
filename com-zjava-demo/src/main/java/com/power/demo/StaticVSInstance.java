package com.power.demo;

/**
 * 静态方法和非静态方法运行效率对比
 */
public class StaticVSInstance
{
    public static void staticMethod()
    {

    }

    public void instanceMethod()
    {

    }

    public static void main(String[] args)
    {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000000000; i++)
        {
            StaticVSInstance.staticMethod();
        }
        System.out.println(System.currentTimeMillis()-start);

        start = System.currentTimeMillis();
        StaticVSInstance s1 = new StaticVSInstance();
        for (int i = 0; i < 1000000000 ; i++)
        {
            s1.instanceMethod();
        }
        System.out.println(System.currentTimeMillis()-start);
    }
}
