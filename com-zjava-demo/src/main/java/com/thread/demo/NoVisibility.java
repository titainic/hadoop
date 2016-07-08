package com.thread.demo;

/**
 * Created by titanic on 16-7-8.
 */
public class NoVisibility
{
    private static boolean ready;
    private static int nummber;

    private static class ReaderThread extends Thread
    {
        @Override
        public void run()
        {
            while (!ready)
            {
                System.out.println(nummber);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        new ReaderThread().start();
        Thread.sleep(1000);
        nummber = 42;
        ready = true;
        Thread.sleep(10000);
    }
}
