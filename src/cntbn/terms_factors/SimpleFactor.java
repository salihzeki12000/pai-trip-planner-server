package cntbn.terms_factors;

import java.util.Set;

public interface SimpleFactor extends ContinuousFactor {
    SimpleFactor deepCopy();

    /**
     * return free parameters in the SimpleFactor
     *
     * @return
     */
    Set<Integer> getParameters();

    double getWeight();

    void setWeight(double weight);
}
