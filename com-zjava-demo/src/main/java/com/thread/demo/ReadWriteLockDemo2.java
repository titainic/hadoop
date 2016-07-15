package com.thread.demo;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁使用，
 *  读写锁的机制：
 "读-读"不互斥
 "读-写"互斥
 "写-写"互斥

 即在任何时候必须保证：
 只有一个线程在写入；
 线程正在读取的时候，写入操作等待；
 线程正在写入的时候，其他线程的写入操作和读取操作都要等待；

 */
public class ReadWriteLockDemo2
{
    private int value ;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public void read()
    {
        readLock.lock();

        try
        {
            System.out.println(Thread.currentThread().getName() + " 准备读数据!");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "读出的数据为 :" + value);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            readLock.unlock();
        }
    }

    public void write(int x)
    {
        writeLock.lock();
        try
        {
            System.out.println(Thread.currentThread().getName() + " 准备写数据!");
            Thread.sleep(1000);
            value = x;
            System.out.println(Thread.currentThread().getName() + " 写入的数据: " + value);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            writeLock.unlock();
        }
    }

    public static void main(String[] args)
    {
        final ReadWriteLockDemo2 readWriteLockDemo2 = new ReadWriteLockDemo2();

        Runnable read = new Runnable()
        {
            public void run()
            {
                readWriteLockDemo2.read();
            }
        };

        Runnable write = new Runnable()
        {
            public void run()
            {
                readWriteLockDemo2.write(new Random().nextInt());
            }
        };

        for (int i = 0; i < 3; i++)
        {
            new Thread(write).start();
        }

        for (int i = 0; i < 3; i++)
        {
            new Thread(read).start();
        }

    }
}
