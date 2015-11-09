package com.spark.pairrdd;

import java.io.Serializable;

/**
 * 自定义RDD返回类型
 */
public class CombineByKey
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {

    }

    public static class AvgCount implements Serializable
    {
        public AvgCount(int total, int num)
        {
            this.total = total;
            this.num = num;
        }

        private int total;
        private int num;

        public float avg()
        {
            return total/num;
        }

        public int getTotal()
        {
            return total;
        }

        public void setTotal(int total)
        {
            this.total = total;
        }

        public int getNum()
        {
            return num;
        }

        public void setNum(int num)
        {
            this.num = num;
        }
    }
}


