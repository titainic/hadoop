package com.titanic.bean;


/**
 * Created by titanic on 16-7-27.
 */
public  class PCData
{
    private  long data;


    public void setData(long data)
    {
        this.data = data;
    }

    public long getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return "data"+data;
    }
}
