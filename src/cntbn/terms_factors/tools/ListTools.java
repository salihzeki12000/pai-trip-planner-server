package cntbn.terms_factors.tools;

import cntbn.terms_factors.SimpleGaussian;

import java.util.List;

/**
 * Name: ListTools
 * User: wykwon
 * Date: 13. 2. 24
 * Time: 오후 10:01
 */
public class ListTools {
    /**
     * simpleGaussian list의 원소가 하나뿐일때 하나뿐인 요소를 추출한다.
     *
     * @param simpleGaussianList
     * @return
     */
    public static SimpleGaussian popFromSingleList(List<SimpleGaussian> simpleGaussianList) {
        if (simpleGaussianList.size() != 1) {
            throw new RuntimeException("size of SimpleGaussianList must be 1 " + simpleGaussianList);
        }
        return simpleGaussianList.get(0);
    }
}
