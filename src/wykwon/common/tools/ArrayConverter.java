package wykwon.common.tools;

import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 8. 13
 * Time: 오후 12:32
 * To change this template use File | Settings | File Templates.
 */
public class ArrayConverter {
    private static Logger logger = Logger.getLogger(ArrayConverter.class);

    public static double[] strArrayToDoubleArray(String strArray[]) {
        double ret[] = new double[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            ret[i] = Double.parseDouble(strArray[i]);
        }
        return ret;
    }

    public static double[] strArrayToDoubleArray(String strArray[], int startIdx, int endIdx) {
        double ret[] = new double[endIdx - startIdx + 1];
        for (int i = startIdx; i <= endIdx; i++) {
            ret[i - startIdx] = Double.parseDouble(strArray[i]);
        }
        return ret;
    }

    public static int[] strArrayToIntArray(String strArray[]) {
        int ret[] = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            ret[i] = Integer.parseInt(strArray[i]);
        }
        return ret;
    }

    public static int[] strArrayToIntArray(String strArray[], int startIdx, int endIdx) {
        int ret[] = new int[endIdx - startIdx + 1];
        for (int i = startIdx; i <= endIdx; i++) {
            ret[i - startIdx] = Integer.parseInt(strArray[i]);
        }
        return ret;
    }

    public static void main(String[] args) {
        String dfas[] = {"class", "18.18", "4.4", "7.7"};
        logger.debug(Arrays.toString(ArrayConverter.strArrayToDoubleArray(dfas, 1, 2)));
    }
}
