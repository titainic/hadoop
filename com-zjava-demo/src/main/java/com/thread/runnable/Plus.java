package com.thread.runnable;

import com.thread.bean.Msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 加法计算
 */
public class Plus implements Runnable
{
    public static BlockingQueue<Msg> bq = new LinkedBlockingQueue<Msg>();

    public void run()
    {
        while (true)
        {
            try
            {
                Msg msg = bq.take();
                msg.j = msg.i + msg.j;
                Thread.sleep(10000);
                Multiply.bq.add(msg);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
