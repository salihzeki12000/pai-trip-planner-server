package edu.hanyang.trip_planning.tripHTBN.potential;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 11
 * Time: 오후 1:19
 * To change this template use File | Settings | File Templates.
 */
public class UnivariateIntegral {
    private static Logger logger = Logger.getLogger(UnivariateIntegral.class);
    public UnivariateFunction univariateFunction;

    public UnivariateIntegral(UnivariateFunction univariateFunction) {
        this.univariateFunction = univariateFunction;
    }

    double integal(double lowerLimit, double upperLimit, double step) {
        double sum = 0.0;
        for (double x = lowerLimit; x < upperLimit; x = x + step) {
            sum += step * univariateFunction.value(x);
        }


        return sum;
    }

    public static void gaussianTest() {
        GaussianFunction sg = new GaussianFunction(1, 1);
        UnivariateIntegral integral = new UnivariateIntegral(sg);
        logger.debug("gaussian test:" + integral.integal(0, 100, 0.01));
    }

    public static void main(String[] args) {
        gaussianTest();
        //        double meanX= 17;
//        double meanY= 18;
//
//        double sdX = 1;
//        double sdY=1;
//        double sd = Math.sqrt(sdX*sdX + sdY*sdY);
//
////        GaussianFunction gaussian = new GaussianFunction(meanX-meanY, sd);
//
//        GaussianFunction gaussian = new GaussianFunction(-5, 1);
//
//        UnivariateIntegral integral = new UnivariateIntegral(gaussian);
//
//        UnivariateIntegral uInt = new UnivariateIntegral(new MyFunction1(1));
//        logger.debug(uInt.integal(0, 100, 0.01));
//        logger.debug(integral.integal(-100, 100, 0.1));
//        logger.debug(integral.integal(0, 100, 0.1));


    }
}
