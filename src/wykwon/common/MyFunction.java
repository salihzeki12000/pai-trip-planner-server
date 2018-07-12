package wykwon.common;

/**
 * Created by dummyProblem on 2016-10-29.
 */
public class MyFunction {
    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }
}
