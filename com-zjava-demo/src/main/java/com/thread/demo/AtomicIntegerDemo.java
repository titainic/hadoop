package com.thread.demo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 无锁线程安全，操作 CAS
 */
public class AtomicIntegerDemo
{
    static AtomicInteger i = new AtomicInteger();

    public static class AddThread implements Runnable
    {
        public void run()
        {
            for (int j = 0; j < 10000; j++)
            {
                i.incrementAndGet();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        Thread[] ts = new Thread[10];
        for (int j = 0; j < 10; j++)
        {
            ts[j] = new Thread(new AddThread());
        }
        for (int j = 0; j < 10; j++)
        {
            ts[j].start();
        }
        for (int j = 0; j < 10; j++)
        {
            ts[j].join();
        }
        System.out.println(i);
    }
}
