package cntbn.terms_factors;

import java.util.Set;

public interface ContinuousTerm {
    ContinuousTerm deepCopy();

    Set<Integer> getParameters();

    void shift(double offset);
}
