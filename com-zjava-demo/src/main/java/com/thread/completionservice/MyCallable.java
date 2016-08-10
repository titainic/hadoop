package com.thread.completionservice;

import java.util.concurrent.Callable;

/**
 * Created by titanic on 16-8-10.
 */
public class MyCallable implements Callable<String>
{

    private String useName;
    private long sleepValue;

    public MyCallable(String useName, long sleepValue)
    {
        this.useName = useName;
        this.sleepValue = sleepValue;
    }

    public String call() throws Exception
    {
        System.out.println(useName);
        Thread.sleep(sleepValue);
        return "return " + useName;
    }
}
