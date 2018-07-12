package wykwon.common.tools;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 8. 12
 * Time: 오후 5:10
 * To change this template use File | Settings | File Templates.
 */
public class IntArrayTools {
    public static int sum(int... values) {
        int result = 0;
        for (int value : values)
            result += value;
        return result;
    }


    public static IntPair min(int data[]) {
        int argMin = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            if (data[i] < min) {
                argMin = i;
                min = data[i];
            }
        }
        if (argMin < 0) {
            throw new RuntimeException("error with data " + Arrays.toString(data));
        }
        return new IntPair(argMin, min);
    }

    /**
     * Integer Array의 min을 구한다.
     * data중에 값이 없으면 null로 처리한다.
     * 모든 값이 null이면 argmix=null, minValue=MAX_VALUE가 된다 .
     *
     * @param data
     * @return
     */
    public static IntegerPair min(Integer data[]) {
        Integer argMin = null;
        Integer min = Integer.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                if (data[i] < min) {
                    argMin = i;
                    min = data[i];
                }
            }
        }
        return new IntegerPair(argMin, min);
    }

    /**
     * 길이가 같은 두 double array의 Euclidian distance 를 비교함
     *
     * @param ts1
     * @param ts2
     * @return
     */
    private static int distance(int ts1[], int ts2[]) {
        if (ts1.length != ts2.length) {
            throw new RuntimeException("length does not match");
        }
        int sum = 0;
        for (int i = 0; i < ts1.length; i++) {
            sum += Math.abs(ts1[i] - ts2[i]);
        }
        return sum;
    }
}
