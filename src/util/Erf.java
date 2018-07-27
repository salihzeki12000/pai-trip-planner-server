package util;

import java.util.*;

public class Erf {
    public static double erf(double x) {
        // constants
        final double a1 = 0.254829592;
        final double a2 = -0.284496736;
        final double a3 = 1.421413741;
        final double a4 = -1.453152027;
        final double a5 = 1.061405429;
        final double p = 0.3275911;

        // Save the sign of x
        double sign = 1;
        if (x < 0) {
            sign = -1;
        }
        x = Math.abs(x);

        // A&S formula 7.1.26
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

        return sign * y;
    }

    public static class MyArrays {

        public static double sum(double array[]) {
            double sum = 0;
            for (double v : array) {
                sum = sum + v;
            }
            return sum;
        }


        public static int[] toIntArray(Collection<Integer> set) {
            int ret[] = new int[set.size()];
            int cnt = 0;
            for (Integer i : set) {
                ret[cnt] = i;
                cnt++;
            }
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

        public static double distance(double vec1[], double vec2[]) {
            checkEqualLength(vec1, vec2);
            double sum = 0.0D;

            for (int i = 0; i < vec1.length; ++i) {
                sum += abs(vec1[i] - vec2[i]);
            }

            return sum;
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

        public static double abs(double x) {
            return Double.longBitsToDouble(9223372036854775807L & Double.doubleToRawLongBits(x));
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
    }
}