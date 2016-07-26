package com.thread.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SimpleDateFormat 线程不安全，使用ThreadLocal确保应用层面设置一个对象
 * ThreadLocal是起到一个容器的作用
 */
public class ThreadLocalDemo
{
    public static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>();

    public static class ParseData implements Runnable
    {
        int i = 0;

        public ParseData(int i)
        {
            this.i = i;
        }

        public void run()
        {
            try
            {
                if (tl.get() == null)
                {
                    tl.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
                Date t = tl.get().parse("2015-03-29 19:29:" + i % 60);
                System.out.println(i +":"+t);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++)
        {
            es.execute(new ParseData(i));
        }
    }
}
