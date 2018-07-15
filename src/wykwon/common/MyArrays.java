package wykwon.common;

import cntbn.common.GlobalParameters;
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


    public static double[] toDouble(int[] array) {
        double ret[] = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i];
        }
        return ret;
    }

    public static String[] subArray(String array[], int... indices) {
        String retArray[] = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {

            retArray[i] = array[indices[i]];
        }
        return retArray;
    }


    public static String[] indicesByHeader(String array[], String headers[]) {
//        Arrays.binarySearch()
        return null;
    }

    public static int[] toArray(List<Integer> list) {
        int ret[] = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    public static int[] toArray(Set<Integer> set) {
        int ret[] = new int[set.size()];
        int cnt = 0;
        for (Integer i : set) {
            ret[cnt] = i;
            cnt++;
        }
        return ret;
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

    public static int argMin(double array[]) {
        int argMin = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                argMin = i;
            }
        }
        if (argMin == -1) {
            throw new RuntimeException("error");
        }
        return argMin;
    }

    public static int argMin(int indices[], double array[]) {
        int argMin = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                argMin = i;
            }
        }
        if (argMin == -1) {
            System.out.println(Arrays.toString(array));
            throw new RuntimeException("error");
        }
        return indices[argMin];
    }

    /**
     * Calculates the sum of each row in a matrix.
     *
     * @param v
     * @return Array. value of i'th element is sum of values in column(i)
     */
    public static double[] sum_column(double[][] v) {
        int rowSize = v.length;
        int colSize = v[0].length;
        double[] X = new double[rowSize];
        double s;
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            s = 0;
            for (int colIdx = 0; colIdx < colSize; colIdx++)
                s += v[rowIdx][colIdx];
            X[rowIdx] = s;
        }
        return X;
    }

    /**
     * Calculates the sum of each row in a matrix.
     *
     * @param v
     * @return Array. value of i'th element is sum of values in column(i)
     */
    public static double[] abs_sum_column(double[][] v) {
        int rowSize = v.length;
        int colSize = v[0].length;
        double[] X = new double[rowSize];
        double s;
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            s = 0;
            for (int colIdx = 0; colIdx < colSize; colIdx++)
                s += Math.abs(v[rowIdx][colIdx]);
            X[rowIdx] = s;
        }
        return X;
    }

    public static String[] makeStringArray(String str) {
        String array[] = str.split(",");
        return array;
    }

    public static int[] makeIntArray(String str) {
        String array[] = str.split(",");
        int intArray[] = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = Integer.parseInt(array[i]);
        }
        return intArray;
    }

    public static double[] makeDoubleArray(String str) {
        String array[] = str.split(",");
        double doubleArray[] = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = Double.parseDouble(array[i]);
        }
        return doubleArray;
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

    public static void main(String[] args) {

        String array[] = {"A", "B", "C", "D"};
        System.out.println(Arrays.toString(MyArrays.subArray(array, 1, 3)));

        double array2[] = {0.000001, 0.0002, 0.0000001, 0.000001};
        System.out.println(isFlat(array2));
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

    public static double expectation(double[] values) {
        double sum = 0.0;
        for (int i=0; i<values.length;i++){
            sum+= (double)i*values[i];
        }
        return sum;
    }

    public static Set<String> toStringSet(String strs[]){
        Set<String> set = new HashSet<String>();
        for (String str: strs){
            set.add(str);
        }
        return set;
    }

    public static Set<String> toSet(String strArray[]) {
        Set<String> retSet = new HashSet<String>();
        for (String str: strArray){
            retSet.add(str);
        }
        return retSet;
    }
}
