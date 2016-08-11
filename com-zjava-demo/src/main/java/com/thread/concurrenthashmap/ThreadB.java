package com.thread.concurrenthashmap;

/**
 * Created by titanic on 16-8-11.
 */
public class ThreadB extends Thread
{
    private MyService myService;

    public ThreadB(MyService myService)
    {
        this.myService = myService;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < 100; i++)
        {
            myService.map.put("ThreadB" + (i + 1), "ThreadB" + i + 1);
            System.out.println("ThreadB"+(i+1));
        }
    }
}
