package com.titanic.math;

import com.view.point.PlotViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 泰勒展开
 */
public class Taylor
{

    public static void main(String[] args)
    {
        //cos函数的X,Y
        List<Double> cosx = IntStream.range(0, 100).boxed()
                .map(Integer::doubleValue)
                .map(d -> d / 15).collect(Collectors.toList());
        List<Double> cosy = cosx.stream().map(Math::cos).collect(Collectors.toList());

        List<Double> a12y = new ArrayList<>();
        for (int i = 0; i < cosx.size(); i++)
        {
            Double cc = Math.pow(cosx.get(i), 16) / +20922789888000d +
                    Math.pow(cosx.get(i), 14) / -87178291200d +
                    Math.pow(cosx.get(i), 12) / 479001600 +
                    Math.pow(cosx.get(i), 10) / -3628800 +
                    Math.pow(cosx.get(i), 8) / 40320 +
                    Math.pow(cosx.get(i), 6) / -720 +
                    Math.pow(cosx.get(i), 4) / 24 +
                    Math.pow(cosx.get(i), 2) / -2 + 1;
            a12y.add(cc);
        }

        PlotViewUtils.printxLine(cosx, cosy, a12y);
    }

}
