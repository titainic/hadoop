package com.titanic.ml.lr;

import com.titanic.ml.utils.DataInitUtils;
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
        INDArray y = data.getColumn(1);

        //构建A矩阵
        INDArray x2 = Nd4j.ones( 1,x1.length());
        INDArray x = Nd4j.vstack(x1, x2);

        INDArray xt = x.transpose();

        //矩阵求逆，有疑问
        INDArray xNi = InvertMatrix.invert(x.mmul(xt),true);

        INDArray cc = xt.mul(xNi);

        System.out.println(cc);
//
//        System.out.println(xNi);
//        INDArray y2 = xNi.mmul(y).mmul(xt);

//        System.out.println(y2);
    }
}
