package cntbn.terms_factors;

import java.util.Set;

public interface ContinuousFactor {

    ContinuousFactor deepCopy();

    /**
     * return free paramters in the continuous factor
     *
     * @return
     */
    Set<Integer> getParameters();

    double getMean();

    double getVariance();

    void normalize(double basis);

    boolean hasMixture();

    double getWeight();

    void shift(double offset);



}
