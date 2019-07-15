package com.titanic.ml.lr;

import com.titanic.ml.utils.DataInitUtils;
import com.titanic.ml.utils.PlotViewUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于DL4J，一元线性回归
 */
public class LinearRegressionDL4J
{
    //文件路径
    public static String dataPath = LinearRegressionDL4J.class.getClassLoader().getResource("lr2.csv").getFile();

    //用于打印坐标系点
    public static List<Double> xList = new ArrayList<>();
    public static List<Double> yList = new ArrayList<>();

    //初始化斜率
    public double w = 2;

    //初始化截距
    public double b = 3;

    //初始化学习速度（梯度下降速度）
    public double learningrate = 0.01d;

    //误差范围
    public static double errorSize = 0.01d;




    public static void main(String[] args) throws IOException
    {
        LinearRegressionDL4J model = new LinearRegressionDL4J();

        //初始化加载图像数据
        DataInitUtils.loadCSVinitList(xList,yList,dataPath);

        //加载csv数据,用于计算
        INDArray data = Nd4j.readNumpy(dataPath, ",");
        INDArray x = data.getColumn(0);
        INDArray y = data.getColumn(1);

        double loss = 0;
        while (true)
        {
            loss = fitBGD(x, y, model);

            //当w,b取的值，得到的总体误差最小时，即loss在误差范围内
            if (loss < errorSize)
            {
                break;
            }
        }

        Double wx = model.getW();
        Double bx = model.getB();

        //根据x求出ｙ.y就是预测的值
        List<Double> yLine = new ArrayList<>();
        for (int i = 0; i < xList.size(); i++)
        {
            double xi = xList.get(i);
            //y=kx+b
            Double yL = wx * xi + bx;
            yLine.add(yL);
        }
        PlotViewUtils.xyViewAndLine(xList,yList,yLine,0,12,0,25,loss+"");

    }

    /**
     * 梯度下降计算
     * @param x
     * @param y
     * @param model
     * @return
     */
    public static double fitBGD(INDArray x, INDArray y, LinearRegressionDL4J model)
    {
        double wt = model.getW();
        double bt = model.getB();
        double learningrate = model.getLearningrate();

        INDArray diff = y.dup().sub(x.mul(wt)).sub(bt);
        wt = wt + diff.dup().muli(x).sumNumber().doubleValue() / x.length() * 2 * learningrate;
        bt = bt + diff.sumNumber().doubleValue() / x.length() * 2 * learningrate;

        double loss = Math.pow(calc_error(x, y, model), 2);

        System.out.println("w->" + wt);
        System.out.println("b->" + bt);
        System.out.println("loss->" + loss);
        System.out.println("\n");

        model.setW(wt);
        model.setB(bt);
        return loss;
    }

    //预测函数
    public static INDArray predict(INDArray x, LinearRegressionDL4J model)
    {
        //wx+b
        INDArray yc = x.mul(model.getW()).add(model.getB());
        return yc;
    }

    //误差值
    public static double calc_error(INDArray x, INDArray y, LinearRegressionDL4J model)
    {
        //y-（wx＋b）
        INDArray yc = predict(x, model);
        INDArray error = y.sub(yc);
        return error.sumNumber().doubleValue();
    }


    public double getLearningrate()
    {
        return learningrate;
    }


    public double getW()
    {
        return w;
    }

    public void setW(double w)
    {
        this.w = w;
    }

    public double getB()
    {
        return b;
    }

    public void setB(double b)
    {
        this.b = b;
    }


}