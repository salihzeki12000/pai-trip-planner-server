package cntbn.terms_factors;

import java.util.Set;

public interface SimpleFactor extends ContinuousFactor {
    SimpleFactor deepCopy();
    Set<Integer> getParameters();
    double getWeight();
    void setWeight(double weight);
}
