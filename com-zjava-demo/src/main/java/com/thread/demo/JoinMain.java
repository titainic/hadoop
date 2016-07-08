package com.thread.demo;

/**
 *  输出的i = 0 ,join等待一起执行
 *
 *  主线程等到子线程５秒之后　，等待子线程执行完，然后主线程才执行
 */
public class JoinMain
{
    public volatile static int i = 0;

    public static class AddThread extends Thread
    {
        @Override
        public void run()
        {
            for (int i = 0; i < 10000000; i++);

            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            System.out.println("AddThread" +i);
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        AddThread at = new AddThread();
        at.start();
        System.out.println("main - run");
        at.join();
        System.out.println("main - xxx");
        System.out.println(i);
    }
}
