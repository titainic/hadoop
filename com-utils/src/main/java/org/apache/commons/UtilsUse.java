package org.apache.commons;

import org.apache.commons.lang.ArrayUtils;

/**
 * 各种工具类使用
 */
public class UtilsUse
{
    public static void main(String[] args)
    {
        array();
    }

    /**
     * 数组判断
     */
    private static void array()
    {
        String[] arr = null;
        if(ArrayUtils.isEmpty(arr))
            System.out.println("true");
        else
            System.out.println("false");

        if(ArrayUtils.isNotEmpty(arr))
            System.out.println("true");
        else
            System.out.println("false");
    }
}
