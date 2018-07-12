package edu.hanyang.trip_planning.tripHTBN.potential.util;

import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * vector key를 scalar key로 변환하는 클래스
 * <p/>
 * 예 int vec[2][3] 이라면 0~5 까지 총 6개의 scalar key를 가질 수 있음.
 * 이들의 변환을 처리함
 * <p/>
 * array의 순서는 이걸 호출하기전에 알아서 잘 처리할것
 */
public class ArrayKey {
    private static Logger logger = Logger.getLogger(ArrayKey.class);
    int cardinalities[];
    int totalCardinality;

    public ArrayKey(int... cardinalities) {
        this.cardinalities = cardinalities.clone();
        totalCardinality = 1;
        for (int i = 0; i < cardinalities.length; i++) {
            totalCardinality *= cardinalities[i];
        }
//        logger.debug("totalCardinality=" + totalCardinality);
    }


    /**
     * vector value 를 scalar key로 변환
     *
     * @param values
     * @return
     */
    public int key(int... values) throws RuntimeException{
        if (values.length != cardinalities.length) {
            throw new RuntimeException("Array size mismatch");
        }

        int key = values[0];
        if (values[0] >= (cardinalities[0])) {
            throw new RuntimeException("value exceed cardinality \n values=" + Arrays.toString(values) + "\t cardinality=" + Arrays.toString(cardinalities));
        }
        int digits = 1;
        for (int i = 0; i < values.length - 1; i++) {
            digits *= cardinalities[i];
            key += values[i + 1] * digits;
            if (values[i + 1] >= (cardinalities[i + 1])) {
                throw new RuntimeException("value exceed cardinality \n values=" + Arrays.toString(values) + "\t cardinality=" + Arrays.toString(cardinalities));
            }
        }
        return key;
    }

    public int[] values(int key) {
        /**
         * valueIndices[0]이 가장 앞 자리임
         *
         */
        if (key > totalCardinality) {
            throw new RuntimeException("key>totalCardinality");
        }
        int valueIndices[] = new int[cardinalities.length];
        for (int i = 0; i < cardinalities.length; i++) {
            int divisior = key / cardinalities[i];
            int remains = key % cardinalities[i];
            valueIndices[i] = remains;
            key = divisior;
        }
        return valueIndices;
    }


    public static void main(String[] args) {
        ArrayKey arrayKey = new ArrayKey(2, 3);
        logger.debug("key=" + arrayKey.key(1, 2));
        logger.debug("values=" + Arrays.toString(arrayKey.values(arrayKey.key(1, 2))));

    }

}
