package com.titanic.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.titanic.bean.PCData;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by titanic on 16-7-28.
 */
public class DisruptorMain
{
    public static void main(String[] args) throws InterruptedException
    {
        Executor executor = Executors.newCachedThreadPool();
        PCDataFactory factory = new PCDataFactory();

        //缓冲区大小
        int buffrtSize = 1024;

        Disruptor<PCData> disruptor = new Disruptor<PCData>(factory, //
                                                            buffrtSize,
                                                            executor,
                                                            ProducerType.MULTI,
                                                            //4中策略BlockingWaitStrategy，SleepingWaitStrategy,YieldingWaitStrategy,BusySpinWaitStrategy
                                                            new BlockingWaitStrategy());

        disruptor.handleEventsWithWorkerPool(new Consumer(), new Consumer(), new Consumer(), new Consumer());
        disruptor.start();

        RingBuffer<PCData> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++)
        {
            bb.putLong(0, l);
            producer.pushData(bb);
            Thread.sleep(100);
            System.out.println("add data" + l);
        }
    }
}
