package com.thread.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * java实现原子操作
 *
 * 多线程操作一个共享变量,线程安全
 */
public class Counter
{

    private AtomicInteger atomicI = new AtomicInteger(0);
    private int i = 0 ;

    public static void main(String[] args)
    {
        final Counter cas = new Counter();
        List<Thread> ts = new ArrayList<Thread>();
        long start = System.currentTimeMillis();

        for (int j = 0; j < 100 ; j++)
        {
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    for (int i = 0; i < 1000; i++)
                    {
                        cas.count();
                        cas.safeCount();
                    }
                }
            });
            ts.add(t);
        }
        for (Thread t : ts)
        {
            t.start();
        }

        //等待所有线程执行完
        for (Thread t : ts)
        {
            try
            {
                t.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println(cas.i);
        System.out.println(cas.atomicI.get());
        System.out.println(System.currentTimeMillis() - start);
    }

    //使用CAS实现线程安全的计数器
    private void safeCount()
    {
        for(;;)
        {
            int i = atomicI.get();
            boolean suc = atomicI.compareAndSet(i, ++i);
            if (suc)
            {
                break;
            }
        }
    }

    //非线程安全计数器
    private void count()
    {
        i++;
    }
}
