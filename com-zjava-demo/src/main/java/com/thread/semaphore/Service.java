package com.thread.semaphore;

import java.util.concurrent.Semaphore;

/**
 * 限制程序并发的数量
 *
 * 使用一个许可
 * 释放一个许可
 */
public class Service
{
    private Semaphore semaphore = new Semaphore(1);

    public void  testMethod()
    {
        try
        {
            //使用一个许可
            semaphore.acquire();

            System.out.println(Thread.currentThread().getName() + " begin timer" + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + " end timer" + System.currentTimeMillis());

            //释放一个
            semaphore.release();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
