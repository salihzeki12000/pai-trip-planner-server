package util;

import org.math.array.StatisticSample;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 7. 30
 * Time: 오후 6:14
 * To change this template use File | Settings | File Templates.
 */
public class MyArrays {
    public static final double flatThreshold = 1E-8;

    public static String[] subArray(String array[], int... indices) {
        String retArray[] = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {

            retArray[i] = array[indices[i]];
        }
        return retArray;
    }

    public static double dist(double a[], double b[]) {
        if (a.length != b.length) {
            throw new RuntimeException("array size mismatch");
        }
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return sum / (double) a.length;
    }

    public static int argMax(double array[]) {
        int argMax = -1;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                argMax = i;
            }
        }
        if (argMax == -1) {
            throw new RuntimeException("error");
        }
        return argMax;
    }

    /**
     * array에서 특정 value를 탐색함.
     *
     * @param array
     * @return -1 if search fail, else the locus of the searched array element
     */
    public static int search(int array[], int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }


    public static boolean isFlat(double[] array) {
        double var = StatisticSample.variance(array);
        return var < flatThreshold;
    }

    public static void normalize(double[] values) {
        double sum = 0.0;
        for (int i = 0; i < values.length; i++) {
            sum = sum + values[i];
        }
        double multiflier;
        if (sum < flatThreshold) {
            multiflier = 1.0 / values.length;
            for (int i = 0; i < values.length; i++) {
                values[i] = multiflier;
            }
        } else {
            multiflier = 1.0 / sum;
            for (int i = 0; i < values.length; i++) {
                values[i] = multiflier * values[i];
            }
        }
    }
}
