package com.thread.sum;

import com.thread.bean.Msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 乘法计算
 */
public class Multiply implements Runnable
{
    public static BlockingQueue<Msg> bq = new LinkedBlockingQueue<Msg>();

    public void run()
    {
        while (true)
        {
            try
            {
                Msg msg = bq.take();
                msg.i = msg.i * msg.j;
                Div.bq.add(msg);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}
