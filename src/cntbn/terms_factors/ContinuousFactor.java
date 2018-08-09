package cntbn.terms_factors;

import java.util.Set;

public interface ContinuousFactor {
    ContinuousFactor deepCopy();

    Set<Integer> getParameters();

    double getMean();

    double getVariance();

    void normalize(double basis);

    boolean hasMixture();

    double getWeight();

    void shift(double offset);
}
