package com.titanic.zkclient;

import java.io.Serializable;

/**
 * Created by titanic on 16-10-18.
 */
public class User implements Serializable
{
    private Integer id;
    private String namr;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNamr()
    {
        return namr;
    }

    public void setNamr(String namr)
    {
        this.namr = namr;
    }
}
