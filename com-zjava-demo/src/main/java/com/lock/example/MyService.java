package com.lock.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by titanic on 17-6-28.
 */
public class MyService
{

    public Lock lock = new ReentrantLock();


    public void method()
    {
        lock.lock();
        for (int i = 0; i < 5; i++)
        {
            System.out.println("ThreadName="+Thread.currentThread().getName()+" "+(i+1));
        }
        lock.unlock();
    }
}
