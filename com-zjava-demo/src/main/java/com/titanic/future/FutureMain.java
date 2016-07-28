package com.titanic.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Future运行模式，当FutureTask没有执行完。会一只等待。异步调用
 */
public class FutureMain
{
    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        //构造FutureTask
        FutureTask<String> future = new FutureTask<String>(new RealData("a"));
        ExecutorService es = Executors.newFixedThreadPool(1);

        //在这里开启线程进行RealData的call执行
        es.submit(future);

        System.out.println("请求完毕");
        for (;;)
        {
            if (future.isDone())
            {
                System.out.println("数据="+future.get());
                System.exit(0);
            }
            else
            {
                System.out.println("数据未准备好");
            }
            Thread.sleep(500);
        }

//        Thread.sleep(2000);


    }
}
