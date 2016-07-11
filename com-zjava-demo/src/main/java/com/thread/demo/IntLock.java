package com.thread.demo;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by titanic on 16-7-11.
 */
public class IntLock implements Runnable
{

    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;

    public IntLock(int lock)
    {
        this.lock = lock;
    }

    public void run()
    {
        try
        {
            if (lock == 1)
            {
                lock1.lockInterruptibly();
                Thread.sleep(500);
                lock2.lockInterruptibly();
                System.out.println("1 ->");
            }
            else
            {
                lock2.lockInterruptibly();
                Thread.sleep(500);
                lock1.lockInterruptibly();
                System.out.println("2 ->");
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (lock1.isHeldByCurrentThread())
            {
                lock1.unlock();
            }
            if (lock2.isHeldByCurrentThread())
            {
                lock2.unlock();
            }
            System.out.println(Thread.currentThread().getId()+"：线程退出");
        }

    }

    public static void main(String[] args) throws InterruptedException
    {
        IntLock r1 = new IntLock(1);
        IntLock r2 = new IntLock(2);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        Thread.sleep(1000);
        t2.interrupt();
    }
}
