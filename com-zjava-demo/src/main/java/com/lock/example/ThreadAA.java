package com.lock.example;

/**
 * Created by titanic on 17-6-28.
 */
public class ThreadAA implements Runnable
{
    private MyService2 service2;

    public ThreadAA(MyService2 service2)
    {
        this.service2 = service2;
    }

    public void run()
    {
        service2.methodA();
    }
}
