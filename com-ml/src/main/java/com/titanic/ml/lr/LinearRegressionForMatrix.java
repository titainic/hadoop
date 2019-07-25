package com.titanic.ml.lr;

import com.titanic.ml.utils.DataInitUtils;
import com.view.point.PlotViewUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 多变量线性回归，矩阵解析解形式
 */
public class LinearRegressionForMatrix
{


    public static void main(String[] args) throws IOException
    {
//        unaryLinearRegression();
//        multipleLinearRegression();
    }


    /**
     * 多元线性回归
     * @return
     * @throws IOException
     */
    public static double multipleLinearRegression() throws IOException
    {
        String dataPath = LinearRegressionForMatrix.class.getClassLoader().getResource("lpsa.data").getFile();
        
        INDArray data = Nd4j.readNumpy(dataPath, ",");

        INDArray Y = data.getColumn(0);
        INDArray X = data.getColumns(1, 2, 3, 4, 5, 6, 7, 8);

        INDArray Xtranspose = X.transpose();

        INDArray A = InvertMatrix.invert(Xtranspose.mmul(X), false).mmul(Xtranspose).mmul(Y);

        double loss = Math.pow(Y.sub(X.mmul(A)).sumNumber().doubleValue(), 2);

        return  loss;
    }

    /**
     * 一元线性回归
     */
    public static double unaryLinearRegression() throws IOException
    {
        String dataPath = LinearRegressionForMatrix.class.getClassLoader().getResource("lr2.csv").getFile();

        //用于打印坐标系点
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();

        //初始化加载图像数据
        DataInitUtils.loadCSVinitList(xList, yList, dataPath);
        //加载csv数据,用于计算
        INDArray data = Nd4j.readNumpy(dataPath, ",");

        INDArray x1 = data.getColumn(0);
        INDArray Y = data.getColumn(1);

        INDArray x2 = Nd4j.ones(1, x1.length());

        INDArray xTranspose = Nd4j.vstack(x1, x2);

        //构建X矩阵
        INDArray X = xTranspose.transpose();

        INDArray A = InvertMatrix.invert(xTranspose.mmul(X), false).mmul(xTranspose).mmul(Y);

        double[] aArry = A.data().asDouble();
        double w = aArry[0];
        double b = aArry[1];

        //误差
        double loss = Math.pow(Y.sub(X.mmul(A)).sumNumber().doubleValue(), 2);

        List<Double> yLine = new ArrayList<>();
        for (int i = 0; i < xList.size(); i++)
        {
            double xi = xList.get(i);
            //y=kx+b
            Double yL = w * xi + b;
            yLine.add(yL);
        }
        PlotViewUtils.xyViewAndLine(xList, yList, yLine, 0, 12, 0, 25, loss + "");

        return loss;
    }



}
