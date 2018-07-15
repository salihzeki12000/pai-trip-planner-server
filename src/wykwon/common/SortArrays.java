package wykwon.common;

import java.util.Arrays;

/**
 * Name: sortArray
 * User: dummyProblem
 * Date: 13. 2. 23
 * Time: 오후 8:01
 */
public class SortArrays {

    public static void insertionSort(int indices[], int values[]) {

    }

    /**
     * indice 순으로 value를 정렬한다.
     * 작은값이 앞에 오도록
     * @param indices
     * @param values
     */
    public static void sort(int indices[], int values[]) {
        int j;                     // the number of items sorted so far
        int key;                // the item to be inserted
        int i;
        int keyValue;
        for (j = 1; j < indices.length; j++)    // Start with 1 (not 0)
        {
            key = indices[j];  // 두번째 값부터 선택
            keyValue = values[j];
            for (i = j - 1; (i >= 0) && (indices[i] > key); i--)   // 선택된 값보다 큰 값을 찾는다.
            {
                values[i + 1] = values[i]; // 큰 값을 찾은경우 그 값뒤의 모든 값을 우측으로 이동
                indices[i + 1] = indices[i];

            }
            indices[i + 1] = key;    // Put the key in its proper location
            values[i + 1] = keyValue;

        }
    }

    /**
      * indice 순으로 value를 정렬한다.
     * @param indices 정렬할 값들
     * @param values
     */
    public static void sort(double indices[], int values[]) {
        int j;                     // the number of items sorted so far
        double key;                // the item to be inserted
        int i;
        int keyValue;
        for (j = 1; j < indices.length; j++)    // Start with 1 (not 0)
        {
            key = indices[j];  // 두번째 값부터 선택
            keyValue = values[j];
            for (i = j - 1; (i >= 0) && (indices[i] > key); i--)   // 선택된 값보다 큰 값을 찾는다.
            {
                values[i + 1] = values[i]; // 큰 값을 찾은경우 그 값뒤의 모든 값을 우측으로 이동
                indices[i + 1] = indices[i];

            }
            indices[i + 1] = key;    // Put the key in its proper location
            values[i + 1] = keyValue;

        }
    }
    /**
     * indice 순으로 value를 정렬한다.
     * @param indices 정렬할 값들
     * @param values
     */
    public static void sort(int indices[], double values[]) {
        int j;                     // the number of items sorted so far
        int key;                // the item to be inserted
        int i;
        double keyValue;
        for (j = 1; j < indices.length; j++)    // Start with 1 (not 0)
        {
            key = indices[j];  // 두번째 값부터 선택
            keyValue = values[j];
            for (i = j - 1; (i >= 0) && (indices[i] > key); i--)   // 선택된 값보다 큰 값을 찾는다.
            {
                values[i + 1] = values[i]; // 큰 값을 찾은경우 그 값뒤의 모든 값을 우측으로 이동
                indices[i + 1] = indices[i];

            }
            indices[i + 1] = key;    // Put the key in its proper location
            values[i + 1] = keyValue;

        }
    }
    /**
     * indice 순으로 value를 정렬한다.
     * 큰값이 앞에 오도록 함
     * @param indices 정렬할 값들
     * @param values
     */
    public static void sort_inverse(int indices[], int values[]) {
        int j;                     // the number of items sorted so far
        int key;                // the item to be inserted
        int i;
        int keyValue;
        for (j = 1; j < indices.length; j++)    // Start with 1 (not 0)
        {
            key = indices[j];  // 두번째 값부터 선택
            keyValue = values[j];
            for (i = j - 1; (i >= 0) && (indices[i] < key); i--)   // 선택된 값보다 작은 값을 찾는다.
            {
                values[i + 1] = values[i]; // 큰 값을 찾은경우 그 값뒤의 모든 값을 우측으로 이동
                indices[i + 1] = indices[i];

            }
            indices[i + 1] = key;    // Put the key in its proper location
            values[i + 1] = keyValue;

        }
    }

    public static void sort_inverse(double indices[], int values[]) {
        int j;                     // the number of items sorted so far
        double key;                // the item to be inserted
        int i;
        int keyValue;
        for (j = 1; j < indices.length; j++)    // Start with 1 (not 0)
        {
            key = indices[j];  // 두번째 값부터 선택
            keyValue = values[j];
            for (i = j - 1; (i >= 0) && (indices[i] < key); i--)   // 선택된 값보다 작은 값을 찾는다.
            {
                values[i + 1] = values[i]; // 큰 값을 찾은경우 그 값뒤의 모든 값을 우측으로 이동
                indices[i + 1] = indices[i];

            }
            indices[i + 1] = key;    // Put the key in its proper location
            values[i + 1] = keyValue;

        }
    }

    public static void sort_inverse(int indices[], double values[]) {
        int j;                     // the number of items sorted so far
        int key;                // the item to be inserted
        int i;
        double keyValue;
        for (j = 1; j < indices.length; j++)    // Start with 1 (not 0)
        {
            key = indices[j];  // 두번째 값부터 선택
            keyValue = values[j];
            for (i = j - 1; (i >= 0) && (indices[i] < key); i--)   // 선택된 값보다 작은 값을 찾는다.
            {
                values[i + 1] = values[i]; // 큰 값을 찾은경우 그 값뒤의 모든 값을 우측으로 이동
                indices[i + 1] = indices[i];

            }
            indices[i + 1] = key;    // Put the key in its proper location
            values[i + 1] = keyValue;

        }
    }

    public static int[] makeIndices(int size) {
        int ret[] = new int[size];
        for (int i = 0; i < size; i++) {
            ret[i] = i;
        }
        return ret;
    }


    public static void main(String args[]) {
        int keys[] = {2, 0, 1, -1};
        int values[] = {1, 0, 0, 2};
        SortArrays.sort(keys, values);
        System.out.println(Arrays.toString((keys)));
        System.out.println(Arrays.toString((values)));

        SortArrays.sort_inverse(keys, values);
        System.out.println(Arrays.toString((keys)));
        System.out.println(Arrays.toString((values)));

    }


}
