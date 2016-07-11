package com.thread.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号量操作。
 */
public class SemapDemo implements Runnable
{
    //允许操作的许可　５
    final Semaphore semap = new Semaphore(5);

    public void run()
    {
        try
        {
            //零界区
            semap.acquire();
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId()+":done!");
            //零界区
            semap.release();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        ExecutorService exec = Executors.newFixedThreadPool(20);
        final SemapDemo demo = new SemapDemo();
        for (int i = 0; i < 20; i++)
        {
            exec.submit(demo);
        }
    }
}
