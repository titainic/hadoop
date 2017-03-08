package com.power.demo;

import java.util.concurrent.TimeUnit;

/**
 * Created by titanic on 17-3-6.
 */
public class SleepUtils
{
    public static final void second(long second)
    {
        try
        {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
