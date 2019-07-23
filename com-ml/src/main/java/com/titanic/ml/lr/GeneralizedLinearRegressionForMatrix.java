package com.titanic.ml.lr;

import com.titanic.ml.utils.DataInitUtils;
import com.titanic.ml.utils.PlotViewUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 广义线性回归，线性方程组求解
 */
public class GeneralizedLinearRegressionForMatrix
{

    //文件路径
    public static String dataPath = GeneralizedLinearRegressionForMatrix.class.getClassLoader().getResource("lr2.csv").getFile();

    public static void main(String[] args) throws IOException
    {
        GeneralizedLinearRegressionForMatrix model = new GeneralizedLinearRegressionForMatrix();

        //用于打印坐标系点
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();

        //初始化加载图像数据
        DataInitUtils.loadCSVinitList(xList,yList,dataPath);

        //加载csv数据,用于计算
        INDArray data = Nd4j.readNumpy(dataPath, ",");



        INDArray x1 = data.getColumn(0);
        INDArray Y = data.getColumn(1);

        //构建X矩阵
        INDArray x2 = Nd4j.ones( 1,x1.length());

        INDArray xTranspose = Nd4j.vstack(x1, x2);

        INDArray X = xTranspose.transpose();

        xTranspose.mmul(X);

        INDArray xNi = InvertMatrix.invert(xTranspose.mmul(X),false);

        INDArray A = xNi.mmul(xTranspose).mmul(Y);

        double[] aArry =   A.data().asDouble();
        double w = aArry[0];
        double b = aArry[1];

        INDArray cc = X.mmul(A);

        double error = Y.sub(cc).sumNumber().doubleValue();
        System.out.println(error);
        double loss = Math.pow(error,2);



        List<Double> yLine = new ArrayList<>();
        for (int i = 0; i < xList.size(); i++)
        {
            double xi = xList.get(i);
            //y=kx+b
            Double yL = w * xi + b;
            yLine.add(yL);
        }
        PlotViewUtils.xyViewAndLine(xList,yList,yLine,0,12,0,25,loss+"");

    }


}
