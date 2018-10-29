package com.dl.neuralent;

import com.dl.math.IActivationFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 人工神经元
 */
public class Neuron
{
    /**
     * 权重
     */
    private List<Double> weight;

    /**
     * 输入
     */
    private List<Double> input;

    /**
     * 输出
     */
    private Double output;

    /**
     * 传递激活函数的值
     */
    private Double outputBeforeActivation;

    /**
     * 神经元的偏倚。除了第一层.它应该总是1.0
     */
    private Double bias = 1.0;

    /**
     * 输入的数量。如果为0，则表示神经元尚未初始化
     */
    private int numberOfInput = 0;

    /**
     * 激活这个神经元
     */
    private IActivationFunction activationFunction;

    public Neuron(int numberOfInput)
    {
        this.numberOfInput = numberOfInput;
        this.weight = new ArrayList<Double>(numberOfInput + 1);
        this.input = new ArrayList<Double>(numberOfInput);
    }

    /**
     * 当实例化神经元时，需要指定输入数据的个数及激活函数
     * @param numberOfInput
     * @param activationFunction
     */
    public Neuron(int numberOfInput, IActivationFunction activationFunction)
    {
        this.numberOfInput = numberOfInput;
        this.weight = new ArrayList<Double>(numberOfInput + 1);
        this.input = new ArrayList<Double>(numberOfInput);
        this.activationFunction = activationFunction;
    }

    /**
     * 计算神经元的输出
     */
    public void calc()
    {
        outputBeforeActivation = 0.0;
        if (numberOfInput > 0)
        {
            if (input != null && weight != null)
            {
                for (int i = 0; i < numberOfInput; i++)
                {
//                    outputBeforeActivation += (i == numberOfInput ? bias : input.get(i)) * weight.get(i);

                    if (i == numberOfInput)
                    {
                        outputBeforeActivation = outputBeforeActivation + bias * weight.get(i);
                    }
                    else
                    {
                        outputBeforeActivation = outputBeforeActivation + input.get(i) * weight.get(i);
                    }
                }
            }
        }
        output = activationFunction.cala(outputBeforeActivation);
    }

}
