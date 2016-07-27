package com.titanic.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.titanic.bean.PCData;

import java.nio.ByteBuffer;

/**
 * Created by titanic on 16-7-27.
 */
public class Producer
{
    private final RingBuffer<PCData> ringBuffer;

    public Producer(RingBuffer<PCData> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void pushData(ByteBuffer bb)
    {
        long sequence = ringBuffer.next();
        try
        {
            PCData event = ringBuffer.get(sequence);
            event.setData(bb.getLong(0));
        }
        finally
        {
            ringBuffer.publish(sequence);
        }
    }



}
