package com.lock.example;

/**
 * Created by titanic on 17-6-28.
 */
public class Run2
{
    public static void main(String[] args) throws InterruptedException
    {
        MyService2 service2 = new MyService2();
        Thread a = new Thread(new com.lock.example.ThreadA(service2));
        a.setName("A");
        a.start();

        Thread aa = new Thread(new com.lock.example.ThreadAA(service2));
        aa.setName("AA");
        aa.start();

        Thread.sleep(100);

        Thread b = new Thread(new com.lock.example.ThreadB(service2));
        b.setName("B");
        b.start();

        Thread bb = new Thread(new com.lock.example.ThreadBB(service2));
        bb.setName("BB");
        bb.start();
    }

}
