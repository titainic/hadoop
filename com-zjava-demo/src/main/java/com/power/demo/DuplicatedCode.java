package com.power.demo;

/**
 * 很小的性能提升
 */
public class DuplicatedCode
{
    public static void beforeTuning()
    {
        long start = System.currentTimeMillis();
        double a1 = Math.random();
        double a2 = Math.random();
        double a3 = Math.random();
        double a4 = Math.random();
        double b1, b2;

        for (int i = 0; i < 1000000000 ; i++)
        {
            b1 = a1 * a2  * a4 / 3 *a3 * a4;
            b2 = a1 * a2  * a4 / 3 *a3 * a4;
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public static void afterTuning()
    {
        long start = System.currentTimeMillis();
        double a1 = Math.random();
        double a2 = Math.random();
        double a3 = Math.random();
        double a4 = Math.random();
        double combine, b1, b2;

        for (int i = 0; i < 1000000000 ; i++)
        {
            combine = a1 * a2  * a4 / 3 *a3 * a4;
            b1 = combine;
            b2 = combine;
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public static void main(String[] args)
    {
        DuplicatedCode.beforeTuning();
        DuplicatedCode.afterTuning();
    }
}
