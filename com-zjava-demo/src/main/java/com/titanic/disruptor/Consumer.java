package com.titanic.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.titanic.bean.PCData;

/**
 * 读取数据已经由disruptor封装，onEvent方法为框架回调方法，只需要简单的处理数据就可以了
 *
 */
public class Consumer implements WorkHandler<PCData>
{

    public void onEvent(PCData pcData) throws Exception
    {
        System.out.println(Thread.currentThread().getId()+":Event: --"+pcData.getData() +"--");
    }
}
