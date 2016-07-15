package com.thread.demo;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒计数器,new CountDownLatch(10);  end.await()等待10个线程执行countDown();，之后继续执行
 */
public class CountDownLatchDemo implements Runnable
{

    static final CountDownLatch end = new CountDownLatch(10);
    static final CountDownLatchDemo demo = new CountDownLatchDemo();

    public void run()
    {
        try
        {
            Thread.sleep(new Random().nextInt(10)*1000);
            System.out.println("check complete");
            end.countDown();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        ExecutorService exec = Executors.newFixedThreadPool(10);
        RunBackGroup runBackGroup = new RunBackGroup();

        for (int i = 0; i < 9; i++)
        {
            exec.submit(demo);
        }
        Thread t = new Thread(runBackGroup);
        t.setDaemon(true);
        exec.submit(t);

        //等待检查
        end.await();
        System.out.println("fire->发射");
        exec.shutdown();
    }
    public static class RunBackGroup implements Runnable
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(1000);
                    System.out.println("主线程在等待");
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }
}
