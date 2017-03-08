package com.power.demo;

import java.util.concurrent.TimeUnit;

/**
 * 线程等待状态,jps查看线程,然后jstack 查看线程ID
 */
public class ThreadState
{
    public static void main(String[] args)
    {
        new Thread(new TimeWaiting(),"TimeWaitingTHread").start();
        new Thread(new Waiting(),"Waiting").start();
        new Thread(new Blocked(),"Blocked-1").start();
        new Thread(new Blocked(),"Blocked-2").start();
    }

    static class TimeWaiting implements  Runnable
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Waiting implements Runnable
    {
        public void run()
        {
            while (true)
            {
                synchronized (Waiting.class)
                {
                    try
                    {
                        Waiting.class.wait();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Blocked implements Runnable
    {
        public void run()
        {
            synchronized (Blocked.class)
            {
                while (true)
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
