package com.thread.demo;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁
 */
public class ReenterLockx implements Runnable
{
    //重入锁
    public static ReentrantLock lock = new ReentrantLock();

    public static int i = 0;

    public void run()
    {
        for (int j = 0; j < 10000000; j++)
        {
            lock.lock();

            try{
                i++;
            }
            finally
            {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        ReenterLockx tl = new ReenterLockx();
        Thread t1 = new Thread(tl);
        Thread t2 = new Thread(tl);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
