package com.power.demo;

/**
 * 逻辑判断，优于位判断
 */
public class OperationCompare
{
    public static void booleanOperation()
    {
        long start = System.currentTimeMillis();
        boolean a = false;
        boolean b = true;
        int c = 0;

        for (int i = 0; i < 1000000; i++)
        {
            if (a & b & "Test_!23".contains("123"))
            {
                c = 1;
            }
        }
        System.out.println(System.currentTimeMillis()-start);
    }

    public static void bitOperate()
    {
        long start = System.currentTimeMillis();
        boolean a = false;
        boolean b = true;
        int c = 0;
        for (int i = 0; i < 1000000; i++)
        {
            if (a && b && "Test_123".contains("123"))
            {
                c = 1;
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public static void main(String[] args)
    {
        OperationCompare.booleanOperation();
        OperationCompare.bitOperate();
    }

}
