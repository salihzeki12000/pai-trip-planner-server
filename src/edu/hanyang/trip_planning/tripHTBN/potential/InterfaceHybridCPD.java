package edu.hanyang.trip_planning.tripHTBN.potential;

import cntbn.terms_factors.ContinuousFactor;

/**
 * Created by wykwon on 2015-09-25.
 */
public interface InterfaceHybridCPD {

    /**
     * CLG CPD의 분포를 획득한다.
     * discrete 부모 노드와 continuous 부모 노드들을 입력으로..
     *
     * @param discreteParentIndices
     * @param discreteParentValues
     * @return
     */
    ContinuousFactor getDistribution(int[] discreteParentIndices, int[] discreteParentValues);


    /**
     * superset으로부터 CLG CPD분포를 획득함
     * <p/>
     * node들과 value들이 주어졌을때 확률 값을 반환함.
     * <p/>
     * enumeration 할때 활용됨
     * <p/>
     * 이 CPT의 모든 node set은 입력인 nodeIndices의 부분집합이어야 함.
     * <p/>
     * theNode와 parent Node들의 구분이 없음*
     *
     * @param discreteParentIndices
     * @param discreteParentValues
     * @return
     */
    ContinuousFactor getDistributionFromSuperset(int discreteParentIndices[], int discreteParentValues[]);
}
