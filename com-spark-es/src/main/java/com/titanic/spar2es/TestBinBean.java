package com.titanic.spar2es;

import java.io.Serializable;

/**
 * Created by titanic on 16-6-30.
 */
public class TestBinBean implements Serializable
{
    private int id;
    private String name;
    private String tel;
    private int age;

    public TestBinBean(int id, String name, int age, String tel)
    {
        this.id = id;
        this.name = name;
        this.age = age;
        this.tel = tel;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }
}
