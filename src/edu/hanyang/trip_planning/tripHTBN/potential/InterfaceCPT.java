package edu.hanyang.trip_planning.tripHTBN.potential;

/**
 * Created by wykwon on 2015-09-25.
 */
public interface InterfaceCPT {
    /**
     * condition들이 주어졌을때 확률 분포를 반환함
     *
     * @param theParentNodeIndices
     * @param parentValues
     * @return
     */

    double[] getDistribution(int theParentNodeIndices[], int parentValues[]);


    /**
     * condition과, chile node의 value가 주어졌을때 확률 분포를 반환함
     *
     * @param theNodeValue         theNode의 value
     * @param theParentNodeIndices parent Node들, 오름차순 정렬 필요
     * @param parentValues         parentValue들
     * @return
     */
    double getProbability(int theNodeValue, int theParentNodeIndices[], int parentValues[]);


    /**
     * node들과 value들이 주어졌을때 확률 값을 반환함.
     * <p/>
     * enumeration 할때 활용됨
     * <p/>
     * 이 CPT의 모든 node set은 입력인 nodeIndices의 부분집합이어야 함.
     * <p/>
     * theNode와 parent Node들의 구분이 없음
     *
     * @param nodeIndices CPT의 node set의 superset , 오름차순 정렬이 되어 있어야 함
     * @param values
     * @return
     */

    double getProbabilityFromSuperset(int nodeIndices[], int values[]);

}
