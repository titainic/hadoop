package com.thread.concurrenthashmap;

/**
 * Created by titanic on 16-8-11.
 */
public class ThreadA extends Thread
{
    private MyService myService;

    public ThreadA(MyService myService)
    {
        this.myService = myService;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < 100; i++)
        {
            myService.map.put("ThreadA" + (i + 1), "ThreadA" + i + 1);
            System.out.println("ThreadA"+(i+1));
        }
    }
}
