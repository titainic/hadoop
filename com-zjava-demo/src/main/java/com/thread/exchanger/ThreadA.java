package com.thread.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * 线程A发送数据到线程B
 */
public class ThreadA extends Thread
{
    List<Integer> list = new ArrayList<Integer>();
    Exchanger<List<Integer>> exchanger = null;

    public ThreadA(Exchanger<List<Integer>> exchanger)
    {
        super();
        this.exchanger = exchanger;
    }

    @Override
    public void run()
    {
        Random rand = new Random();
        for (int i = 0; i < 10; i++)
        {
            list.clear();
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            try
            {
                list = exchanger.exchange(list);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
