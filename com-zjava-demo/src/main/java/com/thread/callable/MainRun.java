package com.thread.callable;

import java.util.concurrent.*;

/**
 * 返回值，处理
 */
public class MainRun
{
    public static void main(String[] args)
    {
        try
        {

            MyCallable callable = new MyCallable(100);
            ExecutorService es = Executors.newCachedThreadPool();
            Future<String> future = es.submit(callable);
            System.out.println("main A " + System.currentTimeMillis());
            System.out.println(future.get());
            System.out.println("main B " + System.currentTimeMillis());

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
