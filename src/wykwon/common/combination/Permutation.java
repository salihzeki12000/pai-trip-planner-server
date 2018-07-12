package wykwon.common.combination;

import wykwon.common.array.MyArrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dummyProblem on 2016-07-28.
 */
public class Permutation {

    public static List<int[]> permute(int... array){
        List<int[]> retArrayList = new ArrayList<>();
        return permute(array.clone(),0, retArrayList);
    }
    public static List<int[]> permute(List<Integer> arr){
        List<int[]> retArrayList = new ArrayList<>();
        permute(arr,0, retArrayList);
//        for (int i=0; i<retArrayList.size();i++){
//            Arrays.sort(retArrayList.get(i));
//        }
        return retArrayList;
    }
    public static List<int[]> permute(List<Integer> arr, int k, List<int[]> retArrayList) {
        for (int i = k; i < arr.size(); i++) {
            java.util.Collections.swap(arr, i, k);
            permute(arr, k + 1, retArrayList);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            retArrayList.add(MyArrays.toIntArray(arr));
        }
        return retArrayList;
    }

    public static List<int[]> permute(int arr[], int k, List<int[]> retArrayList) {

        for (int i = k; i < arr.length; i++) {
            int tmp_i = arr[i];
            int tmp_k = arr[k];
            arr[i] = tmp_k;
            arr[k] = tmp_i ;
            //java.util.Collections.swap(arr, i, k);

            permute(arr.clone(), k + 1, retArrayList);

//            java.util.Collections.swap(arr, k, i);
            tmp_i = arr[i];
            tmp_k = arr[k];
            arr[i] = tmp_k;
            arr[k] = tmp_i ;

        }
        if (k == arr.length - 1) {
            retArrayList.add(arr);
        }
        return retArrayList;
    }

    public static void main(String[] args) {

        List<Integer> intList = new ArrayList<>();
        intList.add(1);intList.add(2);intList.add(3);
        List<int[]> permutations = Permutation.permute(intList);
        for (int[] array: permutations){
            System.out.println(Arrays.toString(array));
        }
        System.out.println("\n\n\n");
        permutations = Permutation.permute(1,2,3);
        for (int[] array: permutations){
            System.out.println(Arrays.toString(array));
        }
    }

}
