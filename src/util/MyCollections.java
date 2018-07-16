package util;

import java.util.Collection;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 15
 * Time: 오후 4:35
 * To change this template use File | Settings | File Templates.
 */
public class MyCollections {
    public static int argMax(List<Double> list) {
        int argMax = -1;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
                argMax = i;
            }
        }
        if (argMax == -1) {
            throw new RuntimeException("error");
        }
        return argMax;
    }

    public static int argMin(List<Double> list) {
        int argMin = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < min) {
                min = list.get(i);
                argMin = i;
            }
        }
        if (argMin == -1) {
            throw new RuntimeException("error");
        }
        return argMin;
    }

    public static String[] toArray(Collection<String> set) {
        String array[] = new String[set.size()];
        int i = 0;
        for (String str : set) {
            array[i] = str;
            i++;
        }
        return array;
    }

}
