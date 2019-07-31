package com.titanic.ml.lr;

import com.titanic.ml.utils.DataInitUtils;
import com.titanic.ml.utils.PlotViewUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 逻辑回归
 */
public class LogisticRegression
{
    public static String dataPath = LogisticRegression.class.getClassLoader().getResource("logistic.csv").getFile();

    public static void main(String[] args) throws IOException
    {
        INDArray data = Nd4j.readNumpy(dataPath, ",");

        List<Double> AxList = new ArrayList<>();
        List<Double> AyList = new ArrayList<>();

        List<Double> BxList = new ArrayList<>();
        List<Double> ByList = new ArrayList<>();

        INDArray x = data.getColumn(0);
        INDArray y = data.getColumn(0);

        DataInitUtils.loadVSCLogisticData(AxList,AyList,BxList,ByList,dataPath);

        PlotViewUtils.xyViewPoint(AxList,AyList,BxList,ByList,-5,6,-5,17);

    }


}
