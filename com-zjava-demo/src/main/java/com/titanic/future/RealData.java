package com.titanic.future;


import java.util.concurrent.Callable;

/**
 * 异步执行的主要操作
 */
public class RealData implements Callable<String>
{
    private String para;

    public RealData(String para)
    {
        this.para = para;
    }

    public String call() throws Exception
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++)
        {
            sb.append(para);
            Thread.sleep(1000);
            System.out.println(i+":::");
        }
        return sb.toString();
    }
}
