package edu.hanyang.trip_planning.tripHTBN.potential;

import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 24
 * Time: 오후 8:05
 * To change this template use File | Settings | File Templates.
 */
public class PlotFunction {
    public static void plot1D(String name, double values[]) {
        double timePoints[] = values.clone();
        for (int i = 0; i < values.length; i++) {
            timePoints[i] = i;
        }
        Plot2DPanel plot = new Plot2DPanel();
        plot.addLinePlot("my plot", timePoints, values);

        JFrame frame = new JFrame(name);
        frame.setSize(new Dimension(400, 300));
        Container c = frame.getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(plot);
        frame.setVisible(true);
    }


    public static void plot(String name, UnivariateFunction function, double lowerLimit, double upperLimit, double step) {
        java.util.List<Double> timePointList = new ArrayList<Double>();

        for (double t = lowerLimit; t < upperLimit; t = t + step) {
            timePointList.add(t);
        }

        double timePoints[] = listAsArray(timePointList);
        double values[] = new double[timePoints.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = function.value(timePoints[i]);
//            System.out.println(timePoints[i] + "\t" + values[i]);
        }

        Plot2DPanel plot = new Plot2DPanel();
        plot.addLinePlot(name, timePoints, values);

        JFrame frame = new JFrame(name);
        frame.setSize(new Dimension(600, 300));
        Container c = frame.getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(plot);
        frame.setVisible(true);
    }

    public static double[] listAsArray(java.util.List<Double> list) {
        double ret[] = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    public static void main(String[] args) {
        GaussianFunction sg = new GaussianFunction(1, 2);
        double weights[] = {1, 2};
        double biases[] = {2, 1};
        SoftmaxFunction sf = new SoftmaxFunction(weights, biases);
        sf.setIdx(0);
        plot("sg", sf, -30, 30, 0.1);

    }
}
