package com.thread.search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并行搜索,还没有完全搞懂
 */
public class SearchMain
{

    //初始化为-1表示没有找到给定的元素
    static AtomicInteger result = new AtomicInteger(-1);
    static int[] arr = new int[]{9,8,7,4,5,6,4,2,3,4,5,66,44,55,88,77,12,33,4,78,98,54,8,452,6231,5,4,468,4,1,25,6,54,42,1,2415,45};
    static ExecutorService pool = Executors.newCachedThreadPool();
    static final int THREAD_NUM = 2;

    public static void main(String[] args) throws ExecutionException, InterruptedException
    {

        int searchValue = 468;


        int subArrSize = arr.length / THREAD_NUM + 1;
        List<Future<Integer>> re = new ArrayList<Future<Integer>>();
        for (int i = 0; i <arr.length ; i+=subArrSize)
        {
            int end = i + subArrSize;
            if (end > arr.length)
            {
                end = arr.length;
            }
            re.add(pool.submit(new SearchTask(result, arr, i, end, searchValue)));
        }
        for (Future<Integer> fu : re)
        {
            if (fu.get() >= 0)
            {
                //查询到值所在数组的下标
                System.out.println(fu.get());
            }
        }
    }
}
