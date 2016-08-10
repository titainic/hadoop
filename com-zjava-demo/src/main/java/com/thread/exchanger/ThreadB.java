package com.thread.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * 线程B接受线程A的数据
 */
public class ThreadB extends Thread
{

    List<Integer> list = new ArrayList<Integer>();
    Exchanger<List<Integer>> exchanger = null;

    public ThreadB(Exchanger<List<Integer>> exchanger)
    {
        super();
        this.exchanger = exchanger;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < 10; i++)
        {
            try
            {
                list = exchanger.exchange(list);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.print(list.get(0) + ", ");
            System.out.print(list.get(1) + ", ");
            System.out.print(list.get(2) + ", ");
            System.out.print(list.get(3) + ", ");
            System.out.println(list.get(4) + ", ");
        }
    }
}
