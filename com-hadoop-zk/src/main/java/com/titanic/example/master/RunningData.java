package com.titanic.example.master;

import java.io.Serializable;

/**
 * 用于描述WoekServer的基本运行信息
 */
public class RunningData implements Serializable
{
    /**
     * 服务器的属性id
     */
    private long cid;

    /**
     * 服务器的名称
     */
    private String name;

    public long getCid()
    {
        return cid;
    }

    public void setCid(long cid)
    {
        this.cid = cid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
