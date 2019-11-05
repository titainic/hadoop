package com.dl4j.diff.demo;

import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;

/**
 * 自动微分各种函数示例
 */
public class SameDiffExample
{


    public static void main(String[] args) throws IOException
    {

        funationFx2();



    }


    public static void funationFx()
    {
        SameDiff sd = SameDiff.create();

        SDVariable inputX = sd.placeHolder("inputX", DataType.FLOAT, -1, 1);

        //构建函数
        SDVariable f = sd.math.sin(inputX.mul(2)).mul(sd.math.pow(sd.math.pow(inputX, 2).div(1), 3).add(1 / 2));

        INDArray data = Nd4j.arange(1, 100, 1);

        //向前传播
        INDArray y = sd.batchOutput().output(f.getVarName()).input(inputX, data).execSingle();

        System.out.println("正向传播");
        System.out.println(y);
    }

    public static void funationFx2()
    {
        //构建SameDiff实例
        SameDiff sd=SameDiff.create();
        //创建变量x、y
        SDVariable x= sd.var("x");

        //定义函数
        SDVariable f = sd.math.pow(x, 2).mul(2);

        INDArray data = Nd4j.arange(1, 100, 1);
        //给变量var类型x绑定具体值
        x.setArray(data);

        //前向计算函数的值
        System.out.println(sd.batchOutput().output(f.getVarName()).input(f, data).execSingle());

        //后向计算求梯度
        sd.execBackwards(null);
        //打印导数
        System.out.println(x.getGradient().getArr());

    }


}
