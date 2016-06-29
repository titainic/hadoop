package com.google.guava;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 *  验证
 */
public class PreconditionsTest
{
    public static void main(String[] args)
    {
        checkTest();
    }

    //提前检查，提前报错
    private static void checkTest()
    {
        //验证是否为true
        Preconditions.checkArgument(false);

        List<String> list = null;
        //验证是否为空
        Preconditions.checkNotNull(list);

        //检查对象的一些状态，不依赖方法参数。 例如， Iterator可以用来next是否在remove之前被调用。
        Preconditions.checkState(true);
    }


}
