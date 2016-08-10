package com.thread.completionservice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 以异步的方式一边生产新任务，一边处理已完成的任务，这样可以讲执行任务与处理任务分离处理
 * 使用take取得已完成的任务，并按照完成的这些任务的时间顺序处理结果
 */
public class MainRunTest1
{
    public static void main(String[] args)
    {
        MyCallable callable1 = new MyCallable("userName1", 5000);
        MyCallable callable2 = new MyCallable("userName2", 4000);
        MyCallable callable3 = new MyCallable("userName3", 3000);
        MyCallable callable4 = new MyCallable("userName4", 2000);
        MyCallable callable5 = new MyCallable("userName5", 1000);

        List<Callable> callableList = new ArrayList<Callable>();
        callableList.add(callable1);
        callableList.add(callable2);
        callableList.add(callable3);
        callableList.add(callable4);
        callableList.add(callable5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        CompletionService csref = new ExecutorCompletionService(es);

        for (int i = 0; i < 5; i++)
        {
            csref.submit(callableList.get(i));
        }

        for (int i = 0; i < 5; i++)
        {
            System.out.println("等待打印" +(i+1)+" 个返回值");
            try
            {
                System.out.println(csref.take().get());
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            } catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }

    }
}
