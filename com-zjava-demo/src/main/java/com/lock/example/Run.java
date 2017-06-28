package com.lock.example;

/**
 * Created by titanic on 17-6-28.
 */
public class Run
{
    public static void main(String[] args)
    {
        MyService service = new MyService();
        Thread a1 = new Thread(new MyThread(service));
        Thread a2 = new Thread(new MyThread(service));
        Thread a3 = new Thread(new MyThread(service));
        Thread a4 = new Thread(new MyThread(service));
        Thread a5 = new Thread(new MyThread(service));

        a1.start();
        a2.start();
        a3.start();
        a4.start();
        a5.start();

    }
}
