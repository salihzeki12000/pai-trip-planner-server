package edu.hanyang.trip_planning.tripHTBN.potential;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.util.Pair;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 11
 * Time: 오후 1:50
 * To change this template use File | Settings | File Templates.
 */
public class GaussianFunction implements UnivariateFunction {

    Gaussian gaussian;
    double mean;
    double sd;

    public GaussianFunction(double mean, double sd) {
        this.mean = mean;
        this.sd = sd;
        gaussian = new Gaussian(mean, sd);


    }

    @Override
    public double value(double x) {

//        double sqrt2pi = 2.506628274631;
//        double var = sd * sd;
//        double a = 1 / (sd * sqrt2pi);
//        double b = 0.5*(x-mean)*(x-mean)/var;
//        return x*a * Math.exp(-b);
        return gaussian.value(x);
    }

    @Override
    public boolean hasConfidenceInterval() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double lowerConfidenceInterval(double sigma_multiplier) {
        return mean - sigma_multiplier * sd;
    }

    @Override
    public double upperConfidenceInterval(double sigma_multiplier) {
        return mean + sigma_multiplier * sd;
    }

    public Pair<Double, Double> confidenceInterval(double sigma_multiplier) {
        double lower = mean - sigma_multiplier * sd;
        double upper = mean + sigma_multiplier * sd;
        return new Pair<Double, Double>(lower, upper);
    }


}
