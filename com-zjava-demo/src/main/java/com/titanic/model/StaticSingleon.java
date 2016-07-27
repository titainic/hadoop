package com.titanic.model;

/**
 * 单例模式
 * 首先没有锁，这使得在高并发环境下性能优越，其次只有在getInstance方法被第一次调用时
 * StaticSingleon的实例才被创建，因为这种方法巧妙的使用内部类和类的初始化，，内部类
 * SingleonHolder被申明为private，这使得我们不可能在外部访问并初始化，而我们只能在
 * getInstance内部对SingleonHolder类进行初始化，利用虚拟机的类初始化机制创建单利
 */
public class StaticSingleon
{
    private StaticSingleon()
    {
    }

    private static class SingleonHolder
    {
        private static StaticSingleon instance = new StaticSingleon();
    }

    public static StaticSingleon getInstance()
    {
        return SingleonHolder.instance;
    }
}
