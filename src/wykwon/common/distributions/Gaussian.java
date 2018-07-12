package wykwon.common.distributions;

import wykwon.common.Pair;

/**
 * Created by wykwon on 2016-11-06.
 */
public class Gaussian {
    public static Pair<Double,Double> convolution(double mean1, double var1, double mean2, double var2){
        double newMean = mean1+mean2;
        double newVar = (var1+var2);
        return new Pair<>(newMean,newVar);
    }
}
