package com.titanic.ml.lr;

import com.titanic.ml.utils.PlotViewUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于DL4J，一元线性回归
 */
public class LinearRegressionDL4J
{
    public static String dataPath = LinearRegressionDL4J.class.getClassLoader().getResource("lr2.csv").getFile();

    //用于打印坐标系点
    public static List<Double> xList = new ArrayList<>();
    public static List<Double> yList = new ArrayList<>();

    //初始化斜率
    public double w = 125.6;

    //初始化截距
    public double b = 10.3;

    //初始化学习速度（梯度下降速度）
    public double learningrate = 0.1d;

    public static int iteration = 18;


    //初始化加载图像数据
    public LinearRegressionDL4J()
    {
        DataInputStream in = null;
        try
        {
            in = new DataInputStream(new FileInputStream(new File(dataPath)));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String csvRow;
            while ((csvRow = bufferedReader.readLine()) != null)
            {
                String[] strArr = csvRow.split(",");
                xList.add(Double.valueOf(strArr[0]));
                yList.add(Double.valueOf(strArr[1]));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException
    {
        LinearRegressionDL4J model = new LinearRegressionDL4J();

        //加载csv数据,用于计算
        INDArray data = Nd4j.readNumpy(dataPath, ",");
        INDArray x = data.getColumn(0);
        INDArray y = data.getColumn(1);


        for (int i = 0; i < iteration; i++)
        {
            fitBGD(x, y, model);
            System.out.println("i->"+i);
            System.out.println("\n");
        }



        String kstr = model.getW() + "";
        model.setW(Double.valueOf(kstr.substring(0, 15)));
        String bstr = model.getB() + "";
        model.setB(Double.valueOf(bstr.substring(0, 15)));

        Double wx = model.getW();
        Double bx = model.getB();

        List<Double> yLine = new ArrayList<>();
        for (int i = 0; i < xList.size(); i++)
        {
            double xi = xList.get(i);
            //y=kx+b
            Double yL = wx * xi + bx;
            yLine.add(yL);
        }
        List<Double> yuuu = doublesData(yLine);
        PlotViewUtils.xyViewAndLine(xList,yList,yuuu,0,12,0,25);

    }

    public static void fitBGD(INDArray x, INDArray y, LinearRegressionDL4J model)
    {
        double wt = model.getW();
        double bt = model.getB();
        double learningrate = model.getLearningrate();

        INDArray diff = y.dup().sub(x.mul(wt)).add(bt);
        wt = wt + diff.dup().muli(x).sumNumber().doubleValue() / x.length() * 2 * learningrate;
        bt = bt + diff.sumNumber().doubleValue() / x.length() * 2 * learningrate;


        double loss = Math.pow(calc_error(x, y, model), 2);

        System.out.println("w->" + wt);
        System.out.println("b->" + bt);
        System.out.println("loss->" + loss);


        model.setW(wt);
        model.setB(bt);
    }

    //目标函数
    public static INDArray predict(INDArray x, LinearRegressionDL4J model)
    {
        //wx+b
        INDArray yc = x.mul(model.getW()).add(model.getB());
        return yc;
    }

    //误差值
    public static double calc_error(INDArray x, INDArray y, LinearRegressionDL4J model)
    {
        //y-wx-b
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

    public static List<Double> doublesData(List<Double> list)
    {
        DecimalFormat df = new DecimalFormat("#.0000000000");
        List<Double> xLists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            String str = df.format(list.get(i));
            xLists.add(Double.valueOf(str));
        }
        return xLists;
    }
}
