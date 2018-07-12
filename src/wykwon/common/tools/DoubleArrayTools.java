package wykwon.common.tools;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 8. 12
 * Time: 오후 5:10
 * To change this template use File | Settings | File Templates.
 */
public class DoubleArrayTools {
    public static double sum(double... values) {
        double result = 0;
        for (double value : values)
            result += value;
        return result;
    }

    public static double mean(double... values) {

        double result = 0;
        for (double value : values)
            result += value;
        return result / (double) values.length;
    }

    public static double variance(double values[], double mean) {
        double var = 0.0;
        double invDataSize = 1.0 / values.length;
        for (double value : values) {
            var += (value - mean) * (value - mean) / invDataSize;
        }
        return var;
    }

    public static double sampleVariance(double values[], double mean) {
        double var = 0.0;
        double invDataSize = 1.0 / (values.length - 1);
        for (double value : values) {
            var += (value - mean) * (value - mean) / invDataSize;
        }
        return var;
    }

    public static double standardDeviation(double values[], double mean) {
        return Math.sqrt(variance(values, mean));
    }

    public static double sampleStandardDeviation(double values[], double mean) {
        return Math.sqrt(sampleVariance(values, mean));
    }

    public static double variance(double values[]) {
        double mean = mean(values);
        double var = 0.0;
        double invDataSize = 1.0 / values.length;
        for (double value : values) {
            var += (value - mean) * (value - mean) / invDataSize;
        }
        return var;
    }

    public static double sampleVariance(double values[]) {
        double mean = mean(values);
        double var = 0.0;
        double invDataSize = 1.0 / (values.length - 1);
        for (double value : values) {
            var += (value - mean) * (value - mean) / invDataSize;
        }
        return var;
    }

    public static double standardDeviation(double values[]) {
        return Math.sqrt(variance(values));
    }

    public static double sampleStandardDeviation(double values[]) {
        return Math.sqrt(sampleVariance(values));
    }

    public static KeyValuePair min(double data[]) {
        int argMin = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < data.length; i++) {
            if (data[i] < min) {
                argMin = i;
                min = data[i];
            }
        }
        if (argMin < 0) {
            throw new RuntimeException("error");
        }
        return new KeyValuePair(argMin, min);
    }

    public static KeyValuePair mostSimilar(List<double[]> data, double query[]) {
        int argMin = -1;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < data.size(); i++) {
            double distance = distance(data.get(i), query);
            if (distance < minDistance) {
                argMin = i;
                minDistance = distance;
            }
        }
        if (argMin < 0) {
            throw new RuntimeException("error");
        }
        return new KeyValuePair(argMin, minDistance);


    }

    /**
     * 길이가 같은 두 double array의 Euclidian distance 를 비교함
     *
     * @param ts1
     * @param ts2
     * @return
     */
    private static double distance(double ts1[], double ts2[]) {
        if (ts1.length != ts2.length) {
//        return Double.MAX_VALUE;
            throw new RuntimeException("length does not match:" + Arrays.toString(ts1) + " =/=" +
                    Arrays.toString(ts2));
        }
        double sum = 0.0;
        for (int i = 0; i < ts1.length; i++) {
            sum += Math.abs(ts1[i] - ts2[i]);
        }
        return sum;
    }

    public static double mean(List<Double> values) {
        double result = 0;
        for (double value : values)
            result += value;
        return result / (double) values.size();
    }

    public static double sum(List<Double> values) {
        double result = 0;
        for (double value : values)
            result += value;
        return result;
    }

    public static String toString(double values[]) {
        DecimalFormat df = new DecimalFormat("##.###");
        StringBuffer strbuf = new StringBuffer();
        for (double value : values) {
            strbuf.append(df.format(value) + '\t');
        }
        return strbuf.toString();
    }
}
