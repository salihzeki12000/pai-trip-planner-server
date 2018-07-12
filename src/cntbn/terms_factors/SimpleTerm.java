package cntbn.terms_factors;

import java.util.Set;

public interface SimpleTerm extends ContinuousTerm, Comparable<SimpleTerm> {
    SimpleTerm deepCopy();

    /**
     * Factor
     *
     * @return
     */
    Set<Integer> getParameters();

    double getWeight();

    void setWeight(double weight);


}
