package com.titanic.ml.nl;

import com.titanic.ml.utils.DataInitUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 非线性回归
 */
public class NonlinearregRessionDL4J
{

    public static String dataPath = NonlinearregRessionDL4J.class.getClassLoader().getResource("lr2.csv").getFile();

    Random rr = new Random();

    private double that0 = rr.nextDouble()*10;

    private double that1 = rr.nextDouble()*10;

    private double that2 = rr.nextDouble()*10;

    //初始化学习速度（梯度下降速度）
    public double learningrate = 0.001d;

    //误差范围
    public static double errorSize = 2d;



    public static void main(String[] args) throws IOException
    {

        NonlinearregRessionDL4J model = new NonlinearregRessionDL4J();

        //用于打印坐标系点
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();

        DataInitUtils.loadCSVinitList(xList, yList, dataPath);

        //加载csv数据,用于计算
        INDArray data = Nd4j.readNumpy(dataPath, ",");
        INDArray x = data.getColumn(0);
        INDArray y = data.getColumn(1);

        double loss = 0;

        while (true)
        {
            loss = fitBGD(x,y,model);

            if (loss < errorSize)
            {
                break;
            }
        }



    }

    public static double fitBGD(INDArray x,INDArray y,NonlinearregRessionDL4J model)
    {
        double that0 = model.getThat0();
        double that1 = model.getThat1();
        double that2 = model.getThat2();
        double learningrate = model.getLearningrate();

        INDArray xt = x.mul(x);
        INDArray diff = x.mul(model.getThat1()).add(model.getThat0()).add(x.mul(xt).add(model.getThat2())).sub(y);

        that0= that0 -diff.sumNumber().doubleValue() / x.length() * 2 * learningrate;
        that1= that1 -diff.dup().mul(x).sumNumber().doubleValue() / x.length() * 2 * learningrate;
        that2= that2 -diff.dup().mul(xt).sumNumber().doubleValue() / x.length() * 2 * learningrate;


        double Loss = Math.pow(calc_error(x, y, model), 2);

        System.out.println("that0->" + that0);
        System.out.println("that1->" + that1);
        System.out.println("that2->" + that2);
        System.out.println("Loss->" + Loss);
        System.out.println("\n");

        model.setThat0(that0);
        model.setThat1(that1);
        model.setThat2(that2);

        return Loss;

    }

    //预测函数 that0+that1*x+that2*x^2
    public static INDArray predict(INDArray x, NonlinearregRessionDL4J model)
    {
        INDArray xt = x.mul(x);
        INDArray diff = x.mul(model.getThat1()).add(model.getThat0()).add(x.mul(xt).add(model.getThat2()));
        return diff;
    }

    //误差值
    public static double calc_error(INDArray x, INDArray y, NonlinearregRessionDL4J model)
    {
        //y-（wx＋b）
        INDArray yc = predict(x, model);
        INDArray error = yc.sub(y);
        return error.sumNumber().doubleValue();
    }

    public double getThat0()
    {
        return that0;
    }

    public void setThat0(double that0)
    {
        this.that0 = that0;
    }

    public double getThat1()
    {
        return that1;
    }

    public void setThat1(double that1)
    {
        this.that1 = that1;
    }

    public double getThat2()
    {
        return that2;
    }

    public void setThat2(double that2)
    {
        this.that2 = that2;
    }

    public double getLearningrate()
    {
        return learningrate;
    }

}
