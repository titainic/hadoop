package com.titanic.math;

import com.view.point.PlotViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Taylor
{

    public static void main(String[] args)
    {
        //cos函数的X,Y
        List<Double> cosx = IntStream.range(0, 100).boxed()
                .map(Integer::doubleValue)
                .map(d -> d / 15).collect(Collectors.toList());
        List<Double> cosy = cosx.stream().map(Math::cos).collect(Collectors.toList());


        List<Double> a1y = new ArrayList<>();

        //
        for(int i = 0 ;i< cosx.size();i++)
        {
            Double yprint = 0*cosx.get(i)+1;
            a1y.add(yprint);
        }

        List<Double> a2y = new ArrayList<>();
        //a1x+a2
        for(int i = 0 ;i< cosx.size();i++)
        {
            Double cc = -0.5 * Math.pow(cosx.get(i), 2) +1;
            System.out.println(cc);
            a2y.add(cc);
        }

        PlotViewUtils.printxLine(cosx,cosy,a1y,a2y);
    }


}
