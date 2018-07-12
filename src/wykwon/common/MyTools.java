package wykwon.common;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 8. 24
 * Time: 오후 2:38
 * To change this template use File | Settings | File Templates.
 */
public class MyTools {
    //
    private static Logger logger = Logger.getLogger(MyTools.class);

    /**
     * array가 convex인 경우에 optimize를 수행해서, 최소값을 갖는 index를 반환한다.
     * 내부적으로 minimum이 경계에 있으면 convex가 아니라고 본다.
     *
     * @param values
     * @return 0보다 크면 최소값의 index,  0미만이면 convex가 아님.
     */
    public static int convexOptimization(double values[]) {
        int idx = MyArrays.argMin(values);
        if (idx == 0 || idx == values.length - 1)
//        if (idx==0 || idx==1 || idx == value.length-1 || idx == value.length-2)
        {
            return -1;
        } else {
            return idx;
        }
    }


    public static int convexOptimization(int indices[], double values[]) {
        int idx = MyArrays.argMin(values);
        if (idx == 0 || idx == values.length - 1)
//        if (idx==0 || idx==1 || idx == values.length-1 || idx == values.length-2)
        {
            return -1;
        } else {
            return indices[idx];
        }
    }

    public static int concaveOptimization(double values[]) {
        int idx = MyArrays.argMax(values);
        if (idx == 0 || idx == values.length - 1)
//        if (idx==0 || idx==1 || idx == value.length-1 || idx == value.length-2)
        {
            return -1;
        } else {
            return idx;
        }
    }


    public static int concaveOptimization(int indices[], double values[]) {
        int idx = MyArrays.argMax(values);
        if (idx == 0 || idx == values.length - 1)
//        if (idx==0 || idx==1 || idx == values.length-1 || idx == values.length-2)
        {
            return -1;
        } else {
            return indices[idx];
        }
    }

    public static void main(String[] args) {
        double values[] = {1, 2, 0.9, 4, 5};
        logger.debug("argmin=" + convexOptimization(values));
    }


}
