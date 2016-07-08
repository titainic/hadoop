package com.thread.demo;

/**
 * ｔ被设置为守护线程，系统中只有主线程main为用户线程，因此在main线程休眠２秒之后退出整个程序也随之结束
 * 但如果不把线程ｔ设置为守护线程。main线程结束后,t会不停的打印，永远不会结束
 */
public class DeamonDemo
{
    public static class DeamonT extends Thread
    {
        @Override
        public void run()
        {
            while (true)
            {
                System.out.println("I am alive");
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        Thread t = new DeamonT();
        t.setDaemon(true);
        t.start();
        Thread.sleep(2000);
    }
}
