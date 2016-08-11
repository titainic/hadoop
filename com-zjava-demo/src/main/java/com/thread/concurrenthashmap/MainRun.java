package com.thread.concurrenthashmap;

/**
 * Created by titanic on 16-8-11.
 */
public class MainRun
{
    public static void main(String[] args) throws InterruptedException
    {
        MyService service = new MyService();
        ThreadA a = new ThreadA(service);
        ThreadB b = new ThreadB(service);

        a.start();
        b.start();
        Thread.sleep(5000);
        System.out.println(service.map.size());
    }
}
