package com.power.demo;

/**
 * Created by titanic on 17-3-2.
 */
public class TestJVMStack
{
    private int count = 0;

    public void recursion()
    {
        count++;
        recursion();
    }


    public void testStack()
    {
        try
        {

            recursion();
        } catch (Throwable throwable)
        {
            System.out.println("deep of stack is " + count);
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        TestJVMStack ts = new TestJVMStack();
        ts.testStack();
    }
}
