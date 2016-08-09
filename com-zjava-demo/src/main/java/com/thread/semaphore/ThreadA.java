package com.thread.semaphore;

/**
 * Created by titanic on 16-8-9.
 */
public class ThreadA extends Thread
{
    private Service service;

    public ThreadA(Service service)
    {
        this.service = service;
    }

    @Override
    public void run()
    {
        service.testMethod();
    }
}
