package com.thread.semaphore;

/**
 * 主线程调用
 */
public class MainRun
{
    public static void main(String[] args)
    {
        Service service = new Service();
        Thread a = new ThreadA(service);
        a.setName("A");

        Thread b = new ThreadB(service);
        b.setName("B");

        Thread c = new ThreadC(service);
        c.setName("C");

        a.start();
        b.start();
        c.start();
    }
}
