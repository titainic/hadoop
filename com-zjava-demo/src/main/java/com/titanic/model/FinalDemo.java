package com.titanic.model;

/**
 * 不变模式
 * 不变模式天生就是多线程友好的，她的核心思想就是，一个对象一旦被创建，它的内部状态永远不会发生改变
 * 所以没有一个线程可以修改起内部状态和数据，同事其他内部状态也绝不会自行发生改变
 *
 * final修饰类和属性，没有set方法
 * 构造函数初始化属性
 *
 */
public final class FinalDemo
{
    private final String no;
    private final String name;
    private final double price;

    public FinalDemo(String no, String name, double price)
    {
        this.no = no;
        this.name = name;
        this.price = price;
    }

    public String getNo()
    {
        return no;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }
}
