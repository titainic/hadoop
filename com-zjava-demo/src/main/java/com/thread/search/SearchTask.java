package com.thread.search;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并行查找
 */
public class SearchTask implements Callable<Integer>
{
    private AtomicInteger result;
    private int[] arr;
    private int begin, end, searchValue;


    public SearchTask(AtomicInteger result, int[] arr, int begin, int end, int searchValue)
    {
        this.result = result;
        this.arr = arr;
        this.begin = begin;
        this.end = end;
        this.searchValue = searchValue;
    }

    public Integer call() throws Exception
    {

        int re = search(searchValue, begin, end);
        return null;
    }

    /**
     * @param searchValue
     * @param beginPos
     * @param endPos
     * @return
     */
    private int search(int searchValue, int beginPos, int endPos)
    {
        int i = 0;
        for (i = beginPos; i < endPos; i++)
        {
            //表示其他线程已经找到给定的值
            if (result.get() >= 0)
            {
                return result.get();
            }

            if (arr[i] == searchValue)
            {
                //如果设置失败，表示其他线程已经先找到了
                if (!result.compareAndSet(-1, i))
                {
                    return result.get();
                }
            }
        }
        return -1;
    }

}
