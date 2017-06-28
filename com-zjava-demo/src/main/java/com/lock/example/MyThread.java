package com.lock.example;

/**
 * Created by titanic on 17-6-28.
 */
public class MyThread implements Runnable
{
    private MyService service;

    public MyThread(MyService service)
    {
        this.service = service;
    }

    public void run()
    {
        service.method();
    }
}
