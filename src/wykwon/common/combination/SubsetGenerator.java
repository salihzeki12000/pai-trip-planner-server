package wykwon.common.combination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 4. 24
 * Time: 오전 11:43
 * To change this template use File | Settings | File Templates.
 * <p/>
 * http://rosettacode.org/wiki/Power_set
 */
public class SubsetGenerator implements CombGen {
    private int size;
    private int allMasks;
    private int i = 1;

    public SubsetGenerator(int size) {
        this.size = size;
        this.allMasks = (1 << size);
//        i = 1;
    }

    public boolean hasNext() {
        return i < allMasks;
    }

    public int[] next() {
        List<Integer> tmp = new ArrayList<Integer>();
        for (int j = 0; j < size; j++) {
            if ((i & (1 << j)) > 0) //The j-th element is used
            {
                tmp.add(j + 1);
//                System.out.print((j + 1) + " ");
            }
        }
        i++;

        int ret[] = new int[tmp.size()];
        for (int k = 0; k < tmp.size(); k++) {
            ret[k] = tmp.get(k);
        }
        return ret;
    }

//    public static void powerset(int n) {
//
//        int allMasks = (1 << n);
//        for (int i = 1; i < allMasks; i++) {
//            for (int j = 0; j < n; j++)
//                if ((i & (1 << j)) > 0) //The j-th element is used
//                    System.out.print((j + 1) + " ");
//            System.out.println();
//        }
//
//
//    }

    public static void main(String[] args) {

//        powerset(3);
        SubsetGenerator g = new SubsetGenerator(4);
        while (g.hasNext()) {
            System.out.println(Arrays.toString(g.next()));
        }
    }
}
