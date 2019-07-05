package com.dl4j.example;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import com.github.sh0nk.matplotlib4j.builder.ContourBuilder;
import com.github.sh0nk.matplotlib4j.builder.HistBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Simple plotting methods for the MLPClassifier examples
 *
 * @author Alex Black
 */
public class PlotUtil
{

    private static final boolean DRY_RUN = true;


    public static void main(String[] args) throws IOException, PythonExecutionException
    {
        testPlotScatter();
//        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
//        test();

    }


    public static void test() throws IOException, PythonExecutionException
    {
        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot()
                .add(Arrays.asList(1.3, 20, 200, 300, 400, 1000), Arrays.asList(1, 4, 10, 20, 100, 800))
                .label("label");
//                .linestyle("-");



        plt.xlabel("xlabel");
        plt.ylabel("ylabel");
        plt.text(0.5, 0.2, "text");
        plt.title("Title!");
        plt.legend();
        plt.show();
    }


    public static void testPlotSin() throws IOException, PythonExecutionException {
        List<Double> x = IntStream.range(0, 100).boxed()
                .map(Integer::doubleValue)
                .map(d -> d / 15).collect(Collectors.toList());
        List<Double> y = x.stream().map(Math::sin).collect(Collectors.toList());

        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot()
                .add(x, y)
                .label("sin")
                .linestyle("--");
        plt.xlim(1.0, 5.0);
        plt.title("sin curve");
        plt.legend().loc("upper right");
        plt.show();
    }

    /**
     *
     * @throws IOException
     * @throws PythonExecutionException
     */
    public static void testPlotScatter() throws IOException, PythonExecutionException {

        Plot plt =  Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(Arrays.asList(1.3, 2, 20, 300, 400, 1000), Arrays.asList(1, 4, 10, 20, 100, 800),"o").add(Arrays.asList(1.3, 200));

//        plt.plot().add(Arrays.asList(1, 2, 3), Arrays.asList(1, -8, 27));
        plt.title("scatter");

        plt.show();
    }

    public static void testPlotContour() throws IOException, PythonExecutionException {
        List<Double> x = NumpyUtils.linspace(-1, 1, 100);
        List<Double> y = NumpyUtils.linspace(-1, 1, 100);
        NumpyUtils.Grid<Double> grid = NumpyUtils.meshgrid(x, y);

        List<List<Double>> zCalced = grid.calcZ((xi, yj) -> Math.sqrt(xi * xi + yj * yj));

        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        ContourBuilder contour = plt.contour().add(x, y, zCalced);
        plt.clabel(contour).inline(true).fontsize(10);
        plt.title("contour");
        plt.legend().loc("upper right");
        plt.show();
    }


    public static void testPlotPColor() throws IOException, PythonExecutionException {
        List<Double> x = NumpyUtils.linspace(-1, 1, 100);
        List<Double> y = NumpyUtils.linspace(-1, 1, 100);
        NumpyUtils.Grid<Double> grid = NumpyUtils.meshgrid(x, y);

        List<List<Double>> cCalced = grid.calcZ((xi, yj) -> Math.sqrt(xi * xi + yj * yj));

        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.pcolor().add(x, y, cCalced).cmap("plt.cm.Blues");
        plt.title("pcolor");
        plt.legend().loc("upper right");
        plt.show();
    }

    public static void testPlotOneHistogram() throws IOException, PythonExecutionException {
        Random rand = new Random();
        List<Double> x = IntStream.range(0, 1000).mapToObj(i -> rand.nextGaussian())
                .collect(Collectors.toList());

        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.hist().add(x).orientation(HistBuilder.Orientation.horizontal);
        plt.ylim(-5, 5);
        plt.title("histogram");
        plt.legend().loc("upper right");
        plt.show();
    }

    public static void testPlotTwoHistogram() throws IOException, PythonExecutionException {
        Random rand = new Random();
        List<Double> x1 = IntStream.range(0, 1000).mapToObj(i -> rand.nextGaussian())
                .collect(Collectors.toList());
        List<Double> x2 = IntStream.range(0, 1000).mapToObj(i -> 4.0 + rand.nextGaussian())
                .collect(Collectors.toList());

        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.hist().add(x1).add(x2).bins(20).stacked(true).color("#66DD66", "#6688FF").range(3, 5);
        plt.xlim(-6, 10);
        plt.title("histogram");
        plt.legend().loc("upper right");
        plt.show();
    }

    public static void testPlotHistogramNoXError() throws IOException, PythonExecutionException {

        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.hist();
        plt.xlim(-5, 5);
        plt.title("histogram");
        plt.legend().loc("upper right");
        plt.show();
    }

    public static void testNullCauseNoException() throws IOException, PythonExecutionException {
        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(Arrays.asList(1.3, 0x66, null))
                .add(Arrays.asList(null, -3.2e-8, 1));
        plt.show();
    }

    public static void testShowTwiceClearFirstPlot() throws IOException, PythonExecutionException {
        // TODO: Check .plot() or so is not called twice on the second run script

        Plot plt = Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(Arrays.asList(1.3, 2));
        plt.title("Title!");
        plt.legend();
        plt.show();

        plt.show();
    }

    public static void testSubplots() throws IOException, PythonExecutionException {
        Plot plt =  Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));

//        plt.subplot(2, 1, 1);
        plt.plot()
                .add(Arrays.asList(1, 2 ), Arrays.asList(1, 4));

//        plt.subplot(2, 1, 2);
        plt.plot()
                .add(Arrays.asList(1, 2, 3), Arrays.asList(1, -8, 27));

        plt.show();
    }

    public static void testThirdArgError() throws IOException, PythonExecutionException {


        Plot plt =Plot.create(PythonConfig.pyenvConfig("anaconda3-4.6.11"));
        plt.plot().add(Arrays.asList(1.3, 2))
                .add(Arrays.asList(1.3, 2))
                .add(Arrays.asList(1.3, 2))

                .add(Arrays.asList(1.3, 2));
        plt.show();
    }

}
