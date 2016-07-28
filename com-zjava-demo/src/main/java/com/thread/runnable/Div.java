package com.thread.runnable;

import com.thread.bean.Msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by titanic on 16-7-28.
 */
public class Div implements Runnable
{
    public static BlockingQueue<Msg> bq = new LinkedBlockingQueue<Msg>();

    public void run()
    {
        while (true)
        {
            try
            {
                Msg msg = bq.take();
                msg.i = msg.i / 2;
                System.out.println(msg.orgStr+"="+msg.i);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}
