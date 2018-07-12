package wykwon.common.array;

import org.math.array.IntegerArray;

import java.util.Arrays;

/**
 * Created by dummyProblem on 2016-02-21.
 */
public class IntArrayKey implements Comparable<IntArrayKey>, Confictable<IntArrayKey> {

    private int array[];

    public IntArrayKey(int... array) {
        this.array = array;
        Arrays.sort(this.array);
    }

    public int[] getArray() {
        return array;
    }

    public boolean contain(int value) {
        return Arrays.binarySearch(array, value) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntArrayKey)) return false;

        IntArrayKey intArray = (IntArrayKey) o;

        return Arrays.equals(array, intArray.array);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    public String toString() {
        return Arrays.toString(array);
    }

    /**
     * _로 분리된 string
     *
     * @return
     */
    public String to_String() {
        String ret = IntegerArray.toString("%d_", array);
        ret = ret.replaceAll(" ", "");
        return ret;
    }

    @Override
    public int compareTo(IntArrayKey o) {
        if (Arrays.equals(array, o.array)) {
            return 0;
        }


        if (array.length == o.array.length) {

            for (int i = 0; i < array.length; i++) {
                Integer e1 = array[i];
                Integer e2 = o.array[i];
                if (array[i] != o.array[i]) {
                    return e1.compareTo(e2);
                }
            }

        } else {
            Integer l1 = array.length;
            Integer l2 = o.array.length;
            return l1.compareTo(l2);
        }
        return 0;

    }

    public static IntArrayKey makeSingleElelemtKey(int id) {
        int p[] = new int[1];
        p[0] = id;
        return new IntArrayKey(p);
    }

    @Override
    public boolean isConflict(IntArrayKey intArrayKey) {
        int theArray[] = array.clone();
        Arrays.sort(theArray);
        for (int value : intArrayKey.getArray()) {
            if (Arrays.binarySearch(theArray, value) >= 0) {
                return true;
            }
        }
        return false;
    }
}
