package com.thread.demo;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class FairLock implements Runnable
{
    //重入锁，公平锁，设置，任何线程都有机会执行
    public static ReentrantLock fairLock = new ReentrantLock(true);

    public void run()
    {
        while (true)
        {
            try
            {
                fairLock.lock();
                System.out.println(Thread.currentThread().getName() + "获得锁");
            } finally
            {
                fairLock.unlock();
            }
        }
    }

    public static void main(String[] args)
    {
        FairLock r1 = new FairLock();
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);

        t1.start();
        t2.start();
    }
}
