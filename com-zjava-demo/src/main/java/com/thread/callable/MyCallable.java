package com.thread.callable;

import java.util.concurrent.Callable;

/**
 *
 */
public class MyCallable implements Callable<String>
{
    private int age;

    public MyCallable(int age)
    {
        this.age = age;
    }

    public String call() throws Exception
    {
        Thread.sleep(8000);
        return "返回年龄是："+age;
    }
}
