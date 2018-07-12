package wykwon.common.array;


import org.apache.log4j.Logger;
import org.math.array.StatisticSample;
import wykwon.common.Pair;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dummyProblem
 * Date: 14. 7. 30
 * Time: 오후 6:14
 * To change this template use File | Settings | File Templates.
 */
public class MyArrays {
    private static Logger logger = Logger.getLogger(MyArrays.class);

    public static final double flatThreshold = 1E-5;

    public static int sum(int array[]) {
        int sum = 0;
        for (int v : array) {
            sum = sum + v;
        }
        return sum;
    }

    public static double sum(double array[]) {
        double sum = 0;
        for (double v : array) {
            sum = sum + v;
        }
        return sum;
    }

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

    public static double[] subArray(double array[], int... indices) {
        double retArray[] = new double[indices.length];
        for (int i = 0; i < indices.length; i++) {

            retArray[i] = array[indices[i]];
        }
        return retArray;
    }

    public static double[] standardization(double... values) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (double v : values) {
            if (v > max) {
                max = v;
            }
            if (v < min) {
                min = v;
            }
        }
//        logger.debug("min="+min);
//        logger.debug("max="+max);
        double retValue[] = new double[values.length];
        double range = 1.0 / (max - min);
        for (int i = 0; i < values.length; i++) {
            retValue[i] = (values[i] - min) * range;
        }
        return retValue;
    }


    public static String[] indicesByHeader(String array[], String headers[]) {
//        Arrays.binarySearch()
        return null;
    }

    //    public static int[] toIntArray(Collection<Integer> list) {
//        int ret[] = new int[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            ret[i] = list.get(i);
//        }
//        return ret;
//    }
    public static int[] toIntArray(Collection<Integer> set) {
        int ret[] = new int[set.size()];
        int cnt = 0;
        for (Integer i : set) {
            ret[cnt] = i;
            cnt++;
        }
        return ret;
    }

//    public static double[] toDoubleArray(Collection<Double> doubleList) {
//        double array[] = new double[doubleList.size()];
//        for (int i = 0; i < doubleList.size(); i++) {
//            array[i] = doubleList.get(i);
//        }
//        return array;
//    }

    public static double[] toDoubleArray(Collection<Double> set) {
        double ret[] = new double[set.size()];
        int cnt = 0;
        for (Double i : set) {
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

    public static int dist(int a[], int b[]) {
        if (a.length != b.length) {
            throw new RuntimeException("array size mismatch");
        }
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return sum;
    }

    public static double[] distanceArray(double a[], double b[]) {
        if (a.length != b.length) {
            throw new RuntimeException("array size mismatch");
        }
        double retArray[] = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            retArray[i] = Math.abs(a[i] - b[i]);
        }
        return retArray;
    }

//        public static int[] makeIndicesIncreasing(int size) {
//        int ret[] = new int[size];
//        for (int i = 0; i < size; i++) {
//            ret[i] = i;
//        }
//        return ret;
//    }

    public static int[] makeIndices(int start, int end) {
        if (start >= end) {
            throw new RuntimeException(" start should be less than end");
        }
        int size = end - start + 1;
        int ret[] = new int[size];
        for (int i = start, cnt = 0; i <= end; i++, cnt++) {
            ret[cnt] = i;
        }
        return ret;
    }

    public static int[] makeIndices(int start, int skip, int end) {
        if (start >= end) {
            throw new RuntimeException(" start should be less than end");
        }

        List<Integer> list = new ArrayList<>();
        for (int i = start; i <= end; i = i + skip) {
            list.add(i);
        }
        return MyArrays.toIntArray(list);

    }

    public static double[] getSubArray(double array[], int... indices) {
        double ret[] = new double[indices.length];
        for (int i = 0; i < indices.length; i++) {
            ret[i] = array[indices[i]];
        }
        return ret;
    }

    public static double[] bind(double array1[], double array2[]) {
        int cnt = 0;
        int totalSize = array1.length + array2.length;
        double ret[] = new double[totalSize];


        for (int i = 0; i < array1.length; i++) {
            ret[cnt] = array1[i];
            cnt++;
        }

        for (int i = 0; i < array2.length; i++) {
            ret[cnt] = array2[i];
            cnt++;
        }


        return ret;
    }

    public static double[] bind(List<double[]> arrayList) {
        int cnt = 0;
        int totalSize = 0;
        for (double v[] : arrayList) {
            totalSize += v.length;
        }
        double ret[] = new double[totalSize];
        for (double tmp[] : arrayList) {
            for (int i = 0; i < tmp.length; i++) {
                ret[cnt] = tmp[i];
                cnt++;
            }
        }
        return ret;
    }

    public static Pair<double[], Double> zeroNormalize(double arg[]) {
        double ret[] = new double[arg.length];
        double mean = mean(arg);
        for (int i = 0; i < arg.length; i++) {
            ret[i] = arg[i] - mean;
        }
        return new Pair<double[], Double>(ret, mean);
    }

    public static double[] meanShift(double arg[]) {
        double ret[] = new double[arg.length];
        double mean = mean(arg);
        for (int i = 0; i < arg.length; i++) {
            ret[i] = arg[i] - mean;
        }
        return ret;
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
            logger.fatal(Arrays.toString(array));
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

    public static double max(double array[]) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static double min(double array[]) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
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

    public static double mean(double arg[]) {
        double sum = 0.0;
        for (double v : arg) {
            sum += v;
        }
        return (sum / (double) arg.length);
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

    public static boolean isFlat(double[] array) {
        double sd = Math.sqrt(StatisticSample.variance(array));
        return sd < flatThreshold;
    }

    public static boolean isFlat(double[] array, double flatThreshold) {
        double sd = Math.sqrt(StatisticSample.variance(array));
        return sd < flatThreshold;
    }

    /**
     * 가장 많이 나오는 숫자를 순서대로 배열한다.
     *
     * @param array
     * @param
     * @return
     */
    public static int[] mostFrequent(int array[]) {


        Map<Integer, Integer> frequentMap = new HashMap<>();

        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            Integer count = frequentMap.get(value);
            if (count == null) {
                count = 1;
            } else {
                count = count + 1;
            }
            frequentMap.put(value, count);
        }

        int values[] = new int[frequentMap.size()];
        int counts[] = new int[frequentMap.size()];
        int cnt = 0;
        for (Map.Entry<Integer, Integer> entry : frequentMap.entrySet()) {
            values[cnt] = entry.getKey();
            counts[cnt] = entry.getValue();
            cnt++;
        }

        SortArrays.sort_inverse(counts, values);


        return values;
    }

    /**
     * 값 하나는 count안하도록 하는 함수
     *
     * @param array
     * @param toBeIgnoredValue 이변수는 count하지 말라는 의미임
     * @return
     */
    public static int[] mostFrequentExceptFor(int array[], int toBeIgnoredValue) {

        Map<Integer, Integer> frequentMap = new HashMap<>();

        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            Integer count = frequentMap.get(value);
            if (count == null) {
                count = 1;
            } else {
                count = count + 1;
            }
            if (value != toBeIgnoredValue) {
                frequentMap.put(value, count);
            }
        }

        int values[] = new int[frequentMap.size()];
        int counts[] = new int[frequentMap.size()];
        int cnt = 0;
        for (Map.Entry<Integer, Integer> entry : frequentMap.entrySet()) {
            values[cnt] = entry.getKey();
            counts[cnt] = entry.getValue();
            cnt++;
        }

        SortArrays.sort_inverse(counts, values);


        return values;
    }

    /**
     * 무조건적으로 일정 길이의 배열을 얻어온다.
     * 남으면 짤라내고
     * 비면 defaultValue를 채운다.
     *
     * @param array
     * @param length
     * @param defaultValue
     * @return
     */
    public static int[] padding(int array[], int length, int defaultValue) {
        int ret[] = new int[length];

        for (int i = 0; i < length; i++) {
            if (i < array.length) {
                ret[i] = array[i];
            } else {
                ret[i] = defaultValue;
            }
        }
        return ret;
    }

    public static void decendingSort(int array[]) {
//        Primitive.sort(array, (d1, d2) -> Double.compare(d2, d1), false);
    }

    public static int[] insert(int array[], int value) {
        int ret[] = new int[array.length + 1];
        System.arraycopy(array, 0, ret, 0, array.length);
        ret[ret.length - 1] = value;
        return ret;
    }

    public static String toString(double values[], String format) {
        StringBuffer strbuf = new StringBuffer();
        for (double v : values) {
            strbuf.append(String.format(format, v) + "\t");
        }

        return strbuf.toString();

    }

    public static String toString(int values[], String format) {
        StringBuffer strbuf = new StringBuffer();
        for (double v : values) {
            strbuf.append(String.format(format, v) + "\t");
        }

        return strbuf.toString();

    }


    public static double average(double[] tmp) {
        double sum = 0.0;
        for (double t : tmp) {
            sum += t;
        }
        return sum / (double) tmp.length;
    }

    public static double[] average(List<double[]> arraylist) {
        int size = arraylist.get(0).length;
        double sums[] = new double[size];
        for (double values[] : arraylist) {
            for (int i = 0; i < size; i++) {
                sums[i] = sums[i] + values[i];
            }
        }
        double retArray[] = new double[size];

        for (int i = 0; i < size; i++) {
            retArray[i] = sums[i] / (double) arraylist.size();
        }

        return retArray;
    }

    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }


    public static double distance(double vec1[], double vec2[]) {
        checkEqualLength(vec1, vec2);
        double sum = 0.0D;

        for (int i = 0; i < vec1.length; ++i) {
            sum += abs(vec1[i] - vec2[i]);
        }

        return sum;
//        return distance(vec1, vec2);
    }

    public static void checkEqualLength(double[] a, double[] b) {
        checkEqualLength(a, b, true);
    }

    public static boolean checkEqualLength(double[] a, double[] b, boolean abort) {
        if (a.length == b.length) {
            return true;
        } else if (abort) {
            throw new RuntimeException("dimension mismatch" + a.length + " " + b.length);
        } else {
            return false;
        }
    }

    public static int abs(int x) {
        int i = x >>> 31;
        return (x ^ ~i + 1) + i;
    }

    public static double abs(double x) {
        return Double.longBitsToDouble(9223372036854775807L & Double.doubleToRawLongBits(x));
    }

    public static int[] removeValueFromArray(int array[], int value) {
        List<Integer> retlist = new ArrayList();

        for (int i = 0; i < array.length; i++) {
            if (array[i] != value) {
                retlist.add(array[i]);
            }
        }

        return MyArrays.toIntArray(retlist);
    }


    public static String[] makeString(String str) {
        String array[] = str.split(",");
        return array;
    }

    public static int[] makeInt(String str) {
        String array[] = str.split(",");
        int intArray[] = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = Integer.parseInt(array[i]);
        }
        return intArray;
    }

    public static double[] makeDouble(String str) {
        String array[] = str.split(",");
        double doubleArray[] = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = Double.parseDouble(array[i]);
        }
        return doubleArray;
    }

    public static double expectation(double values[], double probability[]) {
        if (values.length != probability.length) {
            throw new RuntimeException("length mismatch!");
        }
        int length = values.length;
        double sum = 0.0;
        for (int i = 0; i < length; i++) {
            sum += values[i] * probability[i];
        }
        return sum / (double) length;
    }

    public static void main(String[] args) {

//        int array[] = {1, 1, 1, 1, 3, 3, 3, 1, 1, -1, -1};
//        int array2
//        System.out.println(IntegerArray.toString("%3d", array));
//        System.out.println(Arrays.toString(mostFrequentExceptFor(array, -1)));
//        System.out.println(Arrays.toString(mostFrequent(array)));
//        System.out.println(Array.toString(padding(mostFrequent(array), 4, -1)));

        double array1[] = {0.1, 0.2, 0.3};
        double array2[] = {1, 2, 3};
        List<double[]> list = new ArrayList<>();
        list.add(array1);
        list.add(array2);
        System.out.println(distance(array1, array2));
//        System.out.println(Arrays.toString(average(list)));
//        System.out.println(cosineSimilarity(array1,array2));
        //        System.out.println(Arrays.toString(insert(array2, 4)));
    }
}
