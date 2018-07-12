package edu.hanyang.trip_planning.tripHTBN.potential;

/**
 * Created by wykwon on 2015-09-25.
 */
public interface InterfaceSoftmaxCPD {


    /**
     * condition들이 주어졌을때 확률 분포를 반환함
     *
     * @param discreteParentIndices discrete parent들의 indices, 오름차순 정렬
     * @param discreteParentValues
     * @param continuousParentValue
     * @return
     */
    double[] getDistribution(int discreteParentIndices[], int discreteParentValues[], double continuousParentValue);

    /**
     * Condition이 주어졌을때 확률 분포를 반환함
     *
     * @param theValue
     * @param discreteParentIndices
     * @param discreteParentValues
     * @param continuousParentValue
     * @return
     */
    double getProbability(int theValue, int discreteParentIndices[], int discreteParentValues[],
                          double continuousParentValue);

    /**
     * @param discreteParentIndices
     * @param discreteParentValues
     * @param continuousParentValue
     * @return
     */
    double[] getDistributionFromSuperset(int discreteParentIndices[], int discreteParentValues[],
                                         double continuousParentValue);
}
