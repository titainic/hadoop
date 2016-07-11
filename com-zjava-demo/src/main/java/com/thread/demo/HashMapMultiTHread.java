package com.thread.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用jps命令查看这个运行的线程id,然后jstack 可以查看线程运行的状态
 */
public class HashMapMultiTHread
{
    static Map<String, String> map = new HashMap<String, String>();

    public static class AddTHread implements Runnable
    {
        int start;
        public AddTHread(int start)
        {
            this.start = start;
        }

        public void run()
        {
            for (int i = start; i < 100000 ; i += 2)
            {
                map.put(Integer.toString(i), Integer.toBinaryString(i));
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
        Thread t1 = new Thread(new AddTHread(0));
        Thread t2 = new Thread(new AddTHread(1));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(map.size());
    }
}
