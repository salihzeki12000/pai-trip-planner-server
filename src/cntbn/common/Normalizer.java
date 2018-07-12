package cntbn.common;

public class Normalizer {
    public static void normalize(double[] values) {
        double sum = 0.0;
        for (int i = 0; i < values.length; i++) {
            sum = sum + values[i];
        }
        double multiflier;
        if (sum < GlobalParameters.THRESHOLD_ZERO) {
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
