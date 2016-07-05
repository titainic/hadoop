package com.thread.demo;

/**
 * Created by titanic on 16-7-5.
 */
public class Interrupt implements Runnable
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread t = new Thread(new Interrupt());
        Thread.sleep(200);
        t.start();
    }

    public void run()
    {
        while (true)
        {
            if (Thread.currentThread().isInterrupted())
            {
                System.out.println("Interrupte");
                break;
            }
            Thread.yield();
        }
    }
}
