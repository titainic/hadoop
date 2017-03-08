package com.power.demo;

/**
 * Created by titanic on 17-3-6.
 */
public class Demon
{
    public static void main(String[] args)
    {
        Thread thread = new Thread(new DemonRunner(), "DemonRunner");
        thread.setDaemon(true);
        thread.start();
    }

    static class DemonRunner implements Runnable
    {
        public void run()
        {
            try
            {
                SleepUtils.second(10);
            }finally
            {
                System.out.println("DemonThread finally run");
            }
        }
    }
}
