package com.dl.math;

public interface IActivationFunction
{
    /**
     * 此接口表示适用于神经网络的激活函数。任何激活函数都应该实现这个接口，以便用于神经计算
     *
     * @param x
     * @return
     */
    double cala(double x);

    /**
     * 此枚举列出了一些常用的激活函数。该实用程序将此值存储为神经网络属性
     */
    public enum ActivationFunctionENUM
    {
        STEP, LINEAR, SIGMOID, HYPERTAN
    }
}
