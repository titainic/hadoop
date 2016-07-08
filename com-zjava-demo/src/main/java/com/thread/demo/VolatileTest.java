package com.thread.demo;

/**
 * volatile 共享变量，不能被锁
 */
public class VolatileTest
{
    static volatile int i = 0;
    public static class PlusTask implements Runnable
    {
        public void run()
        {
            for (int j = 0; j < 1000; j++)
            {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        Thread[] threads = new Thread[10];
        for (int j = 0; j < 10; j++)
        {
            threads[j] = new Thread(new PlusTask());
            threads[j].start();
        }

        for (int j = 0; j < 10; j++)
        {
            threads[j].join();
        }
        System.out.println(i);
    }
}
