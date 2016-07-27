package com.titanic.disruptor;

import com.lmax.disruptor.EventFactory;
import com.titanic.bean.PCData;

/**
 * Created by titanic on 16-7-27.
 */
public class PCDataFactory implements EventFactory<PCData>
{
    public PCData newInstance()
    {
        return new PCData();
    }
}
