package edu.hanyang.trip_planning.tripHTBN.potential;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 11
 * Time: 오후 1:21
 * To change this template use File | Settings | File Templates.
 */
public interface UnivariateFunction {
    double value(double x);

    boolean hasConfidenceInterval();

    double lowerConfidenceInterval(double sigma_multiplier);

    double upperConfidenceInterval(double sigma_multiplier);
}
