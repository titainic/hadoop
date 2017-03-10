package com.power.demo;

/**
 * 类的成员初始化顺序.重点
 */
public class VarClass
{

    {
        variAbleA = 1;
        //必须等到成员变量申明之后才能调用
//        System.out.println(variAbleA);
    }

    private  int variAbleA = 2;

    public static void main(String[] args)
    {
        VarClass test = new VarClass();
        System.out.println(test.variAbleA);
    }
}
