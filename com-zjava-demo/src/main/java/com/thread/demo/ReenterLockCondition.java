package com.thread.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition中的await()和signal()使用，线程等待和唤醒
 */
public class ReenterLockCondition implements Runnable
{
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    public void run()
    {
        try
        {
            lock.lock();
            condition.await();
            System.out.println("Thread is gong on");
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        ReenterLockCondition tl = new ReenterLockCondition();
        Thread t1 = new Thread(tl);
        t1.start();

        Thread.sleep(2000);

        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
